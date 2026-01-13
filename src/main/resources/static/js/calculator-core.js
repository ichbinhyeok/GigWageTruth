window.createGigCalculator = function (initialData) {

    return {
        selectedApp: initialData.appName || 'Gig App',

        // Inputs (Raw strings to prevent input cursor jumping/glitches)
        rawGross: initialData.gross,
        rawMiles: initialData.miles,
        rawHours: initialData.hours,
        rawTips: 0,
        rawBonuses: 0,
        rawActiveTime: 0,
        rawCustomMpg: 25,
        rawCustomMaintenance: 0.12,
        rawCustomDepreciation: 0.15,
        rawGasPrice: 3.50,
        rawTargetWeeklyIncome: initialData.targetWeeklyIncome || 1000,

        // Safe Getters helpers
        get gross() { return Number(this.rawGross) || 0; },
        get miles() { return Number(this.rawMiles) || 0; },
        get hours() { return Number(this.rawHours) || 0; },
        get tips() { return Number(this.rawTips) || 0; },
        get bonuses() { return Number(this.rawBonuses) || 0; },
        get activeTime() { return Number(this.rawActiveTime) || 0; },
        get waitTime() {
            // Auto-calculate: if user enters active time, wait = total - active
            return this.activeTime > 0 ? Math.max(0, this.hours - this.activeTime) : 0;
        },
        get customMpg() { return Number(this.rawCustomMpg) || 0; },
        get customMaintenance() { return Number(this.rawCustomMaintenance) || 0; },
        get customDepreciation() { return Number(this.rawCustomDepreciation) || 0; },
        get gasPrice() { return Number(this.rawGasPrice) || 0; },
        get targetWeeklyIncome() { return Number(this.rawTargetWeeklyIncome) || 0; },

        // State
        calculationMode: 'standard',
        selectedVehicleId: initialData.defaultVehicle || 'irs-standard',
        vehiclePresets: [],

        // Tax State
        taxRate: 15.3, // Defaults to 15.3%

        // UI State
        showTargetHustle: false,
        showAdvancedOptions: false,
        isRoundTrip: false,
        showShareModal: false,
        shareBtnLabel: 'Share Result',

        async init() {
            try {
                const response = await fetch('/vehicle-presets.json');
                this.vehiclePresets = await response.json();

                // Set initial gas price if standard vehicle is selected
                this.updateGasPriceFromPreset();
            } catch (e) {
                console.error("Failed to load presets", e);
                this.vehiclePresets = [{ id: 'irs-standard', name: 'IRS Standard', costPerMile: 0.725 }];
            }

            // Watch for vehicle changes to update suggestions
            this.$watch('selectedVehicleId', () => this.updateGasPriceFromPreset());
            console.log("GigWageTruth Calculator Loaded: v2 (Features Active)");
        },

        updateGasPriceFromPreset() {
            const v = this.selectedVehicle;
            if (v && v.avgGasPrice) {
                this.rawGasPrice = v.avgGasPrice;
            } else if (v && v.id === 'custom') {
                // Keep existing or default
            }
        },

        get selectedVehicle() {
            return this.vehiclePresets.find(v => v.id === this.selectedVehicleId) || this.vehiclePresets.find(v => v.id === 'irs-standard') || {};
        },

        get effectiveMiles() {
            return this.isRoundTrip ? this.miles * 2 : this.miles;
        },

        // Split Expenses Logic
        get gasCost() {
            if (this.calculationMode === 'standard') return 0; // Standard rate bundles it

            const v = this.selectedVehicle;
            // Electric logic
            if (v.type === 'electric') {
                const kwhUsed = (this.effectiveMiles / 100) * (v.kwhPer100Miles || 25);
                return kwhUsed * (v.avgElectricityCost || 0.15);
            }
            // Bike/Walker
            if (v.type === 'bike' || v.type === 'walker') return 0;

            // Gas/Hybrid
            const mpg = v.id === 'custom' ? this.customMpg : (v.mpg || 25);
            if (mpg <= 0) return 0;
            return (this.effectiveMiles / mpg) * this.gasPrice;
        },

        get otherCost() {
            if (this.calculationMode === 'standard') {
                return this.effectiveMiles * 0.725; // All inclusive
            }

            const v = this.selectedVehicle;
            const maintenance = v.id === 'custom' ? this.customMaintenance : (v.maintenanceCostPerMile || 0);
            const depreciation = v.id === 'custom' ? this.customDepreciation : (v.depreciationCostPerMile || 0);

            return this.effectiveMiles * (maintenance + depreciation);
        },

        get totalDeduction() {
            return this.gasCost + this.otherCost;
        },

        // Dynamic Hero Logic
        get heroTitle() {
            const grandTotal = this.gross + this.tips + this.bonuses;
            if (grandTotal > 2000) return `🚀 $${grandTotal.toFixed(0)} Week?! You're Crushing It!`;
            if (this.tips > 200) return `🦄 Wow! $${this.tips.toFixed(0)} in Tips?`;

            // Default
            return `Real ${this.selectedApp} Pay Calculator`;
        },

        get heroSubtitle() {
            const grandTotal = this.gross + this.tips + this.bonuses;
            if (grandTotal > 2000) return "Top 1% earner behavior. But let's check the Net Profit...";
            if (this.tips > 200) return "Those are unicorn tips! Let's see what you really keep.";
            return `Don't let "Active Time" fool you. Calculate your <strong>true net profit</strong> after expenses.`;
        },

        get totalEarnings() {
            return this.gross + this.tips + this.bonuses;
        },

        get activeDriveTime() {
            // If user provided active time, use it. Otherwise use total hours.
            return this.activeTime > 0 ? this.activeTime : this.hours;
        },

        get taxCost() {
            const profitForTax = this.totalEarnings - this.totalDeduction;
            const rate = this.taxRate / 100;
            return profitForTax <= 0 ? 0 : profitForTax * rate;
        },

        get totalNet() {
            return this.totalEarnings - this.totalDeduction - this.taxCost;
        },

        get netHourly() {
            return this.hours <= 0 ? 0 : this.totalNet / this.hours;
        },

        get annualLossProjection() {
            // Simple projection: If they drive this much every week for 50 weeks
            // How much VALUE are they losing in depreciation (not gas)?
            if (this.calculationMode === 'standard') {
                // IRS rate ~ 40% is depreciation roughly
                return (this.otherCost * 0.40) * 50;
            }
            return this.otherCost * 50;
        },

        // New Engagement Metrics
        get w2Equivalent() {
            // W-2 jobs offer benefits (health, PTO, 401k match, unemployment insurance)
            // typically valued at 30% of salary. Gig work has none.
            // Formula: Net Hourly / 1.25 (roughly 20-25% discount for lack of benefits)
            return Math.max(0, this.netHourly / 1.25);
        },

        get activeHourly() {
            return this.activeDriveTime <= 0 ? 0 : this.totalNet / this.activeDriveTime;
        },

        get requiredGrossForGoal() {
            if (this.targetWeeklyIncome <= 0 || this.gross <= 0 || this.totalNet <= 0) return 0;
            // Calculate current "Net Profit Margin"
            const margin = this.totalNet / this.gross;
            // Required Gross = Target Net / Margin
            return this.targetWeeklyIncome / margin;
        },

        get hoursToGoal() {
            const remaining = this.targetWeeklyIncome - this.totalNet;
            if (remaining <= 0) return 0;
            if (this.netHourly <= 0) return 999; // Infinite if losing money
            return remaining / this.netHourly;
        },

        get shockMessage() {
            const wage = this.netHourly;
            const messages = initialData.shockMessages;

            if (wage < 12) return messages.tier1[0] || { text: "Very Low" };
            if (wage < 15) return messages.tier2[0] || { text: "Below Min Wage" };
            if (wage < 18) return messages.tier3[0] || { text: "Survival Mode" };
            if (wage < 25) return messages.tier4[0] || { text: "Decent" };
            if (wage < 35) return { emoji: '🔥', text: "Top Tier! Great strategy!" };
            return { emoji: '🚀', text: "Unicorn status! Top 1% wage" };
        },

        setMode(mode) {
            this.calculationMode = mode;
            if (mode === 'standard') {
                this.selectedVehicleId = 'irs-standard';
            } else if (this.selectedVehicleId === 'irs-standard') {
                this.selectedVehicleId = initialData.defaultVehicle || 'prius';
            }
        },

        // Shared Logic for generating viral text
        get viralText() {
            return `🚨 Shocking: My real hourly wage on ${this.selectedApp} is only $${this.netHourly.toFixed(2)}/hr (after gas & depreciation). \n\nCalculated with GigVerdict.com 💸 \n\n#GigEconomy #Drivers`;
        },

        shareResult() {
            // Mobile: Try Native Share
            if (navigator.share) {
                navigator.share({
                    title: 'My Real Gig Wage',
                    text: this.viralText,
                    url: 'https://www.gigverdict.com'
                }).catch((err) => {
                    console.log('Native share dismissed/failed', err);
                    // Fallback to modal if native share fails/is cancelled
                    this.showShareModal = true;
                });
            } else {
                // Desktop: Show Modal
                this.showShareModal = true;
            }
        },

        shareTo(platform) {
            const text = encodeURIComponent(this.viralText);
            const url = encodeURIComponent("https://www.gigverdict.com");
            let shareUrl = "";

            switch (platform) {
                case 'twitter':
                    shareUrl = `https://twitter.com/intent/tweet?text=${text}&url=${url}`;
                    break;
                case 'facebook':
                    shareUrl = `https://www.facebook.com/sharer/sharer.php?u=${url}&quote=${text}`;
                    break;
                case 'reddit':
                    shareUrl = `https://www.reddit.com/submit?url=${url}&title=${text}`;
                    break;
                case 'copy':
                    if (navigator.clipboard) {
                        navigator.clipboard.writeText(this.viralText + " https://www.gigverdict.com")
                            .then(() => {
                                alert("Copied to clipboard!"); // Simple feedback for now
                            });
                    }
                    return; // Don't open window
            }

            if (shareUrl) {
                window.open(shareUrl, '_blank', 'width=600,height=400');
            }
        },

        // Legacy compatibility (can be removed later if templates are updated)
        tweetResult() {
            this.shareTo('twitter');
        }
    }
};
