window.createGigCalculator = function (initialData) {
    return {
        selectedApp: initialData.appName || 'Gig App',

        // Inputs
        rawGross: initialData.gross,
        rawMiles: initialData.miles,
        rawHours: initialData.hours,
        rawTips: initialData.tips || 0,
        rawBonuses: initialData.bonuses || 0,
        rawActiveTime: initialData.activeTime || 0,
        rawCustomMpg: initialData.customMpg || 25,
        rawCustomMaintenance: initialData.customMaintenance || 0.12,
        rawCustomDepreciation: initialData.customDepreciation || 0.15,
        rawGasPrice: initialData.gasPrice || 3.50,
        rawTargetWeeklyIncome: initialData.targetWeeklyIncome || 1000,

        // State
        calculationMode: initialData.calculationMode || 'standard',
        selectedVehicleId: initialData.selectedVehicleId || initialData.defaultVehicle || 'irs-standard',
        vehiclePresets: [],
        taxRate: initialData.taxRate || 15.3,
        showTargetHustle: false,
        showAdvancedOptions: false,
        isRoundTrip: Boolean(initialData.roundTrip),
        showShareModal: false,
        shareBtnLabel: 'Share Result',
        savedScenarios: [],
        verdictContainerId: initialData.verdictContainerId,
        debounceTimer: null,

        get gross() { return Number(this.rawGross) || 0; },
        get miles() { return Number(this.rawMiles) || 0; },
        get hours() { return Number(this.rawHours) || 0; },
        get tips() { return Number(this.rawTips) || 0; },
        get bonuses() { return Number(this.rawBonuses) || 0; },
        get activeTime() { return Number(this.rawActiveTime) || 0; },
        get customMpg() { return Number(this.rawCustomMpg) || 0; },
        get customMaintenance() { return Number(this.rawCustomMaintenance) || 0; },
        get customDepreciation() { return Number(this.rawCustomDepreciation) || 0; },
        get gasPrice() { return Number(this.rawGasPrice) || 0; },
        get targetWeeklyIncome() { return Number(this.rawTargetWeeklyIncome) || 0; },

        async init() {
            try {
                const response = await fetch('/vehicle-presets.json');
                this.vehiclePresets = await response.json();
                this.updateGasPriceFromPreset();
            } catch (e) {
                console.error('Failed to load presets', e);
                this.vehiclePresets = [{ id: 'irs-standard', name: 'IRS Standard', type: 'standard', costPerMile: 0.725 }];
            }

            this.$watch('selectedVehicleId', () => this.updateGasPriceFromPreset());
            this.$watch('rawGross', () => this.debouncedFetchVerdict());
            this.$watch('rawMiles', () => this.debouncedFetchVerdict());
            this.$watch('rawHours', () => this.debouncedFetchVerdict());
            this.$watch('rawTips', () => this.debouncedFetchVerdict());
            this.$watch('rawBonuses', () => this.debouncedFetchVerdict());
            this.$watch('rawActiveTime', () => this.debouncedFetchVerdict());
            this.$watch('rawGasPrice', () => this.debouncedFetchVerdict());
            this.$watch('selectedVehicleId', () => this.debouncedFetchVerdict());
            this.$watch('calculationMode', () => this.debouncedFetchVerdict());
            this.$watch('isRoundTrip', () => this.debouncedFetchVerdict());
            this.$watch('rawCustomMpg', () => this.debouncedFetchVerdict());
            this.$watch('rawCustomMaintenance', () => this.debouncedFetchVerdict());
            this.$watch('rawCustomDepreciation', () => this.debouncedFetchVerdict());

            try {
                const stored = localStorage.getItem('gigwager_saved_scenarios');
                if (stored) {
                    this.savedScenarios = JSON.parse(stored);
                }
            } catch (e) {
                console.error('Failed to load local storage', e);
            }
        },

        debouncedFetchVerdict() {
            if (this.debounceTimer) clearTimeout(this.debounceTimer);
            this.debounceTimer = setTimeout(() => this.fetchVerdict(), 500);
        },

        async fetchVerdict() {
            if (!this.verdictContainerId) return;

            const params = this.buildScenarioParams({ app: this.selectedApp });

            try {
                const response = await fetch(`/api/verdict-fragment?${params.toString()}`);
                if (!response.ok) return;

                const html = await response.text();
                const container = document.getElementById(this.verdictContainerId);
                if (container) {
                    container.innerHTML = html;
                }
            } catch (e) {
                console.error('Failed to fetch verdict fragment', e);
            }
        },

        buildScenarioParams(extra = {}, includeKeys = null) {
            const base = {
                app: this.selectedApp.toLowerCase(),
                gross: this.gross,
                miles: this.miles,
                hours: this.hours,
                tips: this.tips,
                bonuses: this.bonuses,
                activeTime: this.activeTime,
                gasPrice: this.gasPrice,
                taxRate: this.taxRate,
                roundTrip: this.isRoundTrip,
                calculationMode: this.calculationMode,
                vehicleId: this.selectedVehicleId,
                customMpg: this.customMpg,
                customMaintenance: this.customMaintenance,
                customDepreciation: this.customDepreciation
            };

            const keys = includeKeys || Object.keys(base);
            const params = new URLSearchParams();

            keys.forEach((key) => {
                const value = base[key];
                if (value === null || value === undefined || value === '') return;
                params.set(key, typeof value === 'boolean' ? String(value) : String(value));
            });

            Object.entries(extra).forEach(([key, value]) => {
                if (value === null || value === undefined || value === '') return;
                params.set(key, typeof value === 'boolean' ? String(value) : String(value));
            });

            return params;
        },

        buildScenarioUrl(path, extra = {}, includeKeys = null) {
            const params = this.buildScenarioParams(extra, includeKeys).toString();
            return params ? `${path}?${params}` : path;
        },

        updateGasPriceFromPreset() {
            if (initialData.gasPrice) {
                this.rawGasPrice = initialData.gasPrice;
                return;
            }

            const vehicle = this.selectedVehicle;
            if (vehicle && vehicle.avgGasPrice) {
                this.rawGasPrice = vehicle.avgGasPrice;
            }
        },

        get selectedVehicle() {
            return this.vehiclePresets.find((vehicle) => vehicle.id === this.selectedVehicleId)
                || this.vehiclePresets.find((vehicle) => vehicle.id === 'irs-standard')
                || {};
        },

        get effectiveMiles() {
            return this.isRoundTrip ? this.miles * 2 : this.miles;
        },

        get totalEarnings() {
            return this.gross + this.tips + this.bonuses;
        },

        get activeDriveTime() {
            return this.activeTime > 0 ? this.activeTime : this.hours;
        },

        get waitTime() {
            return this.activeTime > 0 ? Math.max(0, this.hours - this.activeTime) : 0;
        },

        get gasCost() {
            if (this.calculationMode === 'standard') return 0;

            const vehicle = this.selectedVehicle;
            if (vehicle.type === 'electric') {
                const kwhUsed = (this.effectiveMiles / 100) * (vehicle.kwhPer100Miles || 25);
                return kwhUsed * (vehicle.avgElectricityCost || 0.15);
            }

            if (vehicle.type === 'bike' || vehicle.type === 'walker') return 0;

            const mpg = vehicle.id === 'custom' ? this.customMpg : (vehicle.mpg || 25);
            if (mpg <= 0) return 0;

            return (this.effectiveMiles / mpg) * this.gasPrice;
        },

        get otherCost() {
            if (this.calculationMode === 'standard') {
                return this.effectiveMiles * 0.725;
            }

            const vehicle = this.selectedVehicle;
            const maintenance = vehicle.id === 'custom' ? this.customMaintenance : (vehicle.maintenanceCostPerMile || 0);
            const depreciation = vehicle.id === 'custom' ? this.customDepreciation : (vehicle.depreciationCostPerMile || 0);

            return this.effectiveMiles * (maintenance + depreciation);
        },

        get totalDeduction() {
            return this.gasCost + this.otherCost;
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

        get activeHourly() {
            return this.activeDriveTime <= 0 ? 0 : this.totalNet / this.activeDriveTime;
        },

        get heroTitle() {
            if (this.gross > 0 || this.miles > 0 || this.hours > 0) {
                return `How much did you actually keep on ${this.selectedApp} this week?`;
            }
            return `${this.selectedApp} weekly take-home calculator`;
        },

        get heroSubtitle() {
            return 'See your <strong>weekly take-home</strong>, the tax reserve to protect, and the biggest leak pulling down this week.';
        },

        get biggestLeakLabel() {
            return this.totalDeduction >= this.taxCost ? 'Mileage + car cost' : 'Tax reserve';
        },

        get biggestLeakAmount() {
            return Math.max(this.totalDeduction, this.taxCost);
        },

        get shockMessage() {
            const wage = this.netHourly;

            if (wage < 12) return { label: 'Low margin', text: 'This week likely is not worth it after real costs.' };
            if (wage < 18) return { label: 'Tight margin', text: 'This week needs a fix before the margin slips further.' };
            if (wage < 25) return { label: 'Working', text: 'This week works, but you should still protect the reserve and mileage routine.' };
            if (wage < 35) return { label: 'Strong', text: 'This week is healthy. Protect the margin and scale selectively.' };
            return { label: 'Elite', text: 'This week is strong enough to scale only if the margin stays disciplined.' };
        },

        get annualLossProjection() {
            if (this.calculationMode === 'standard') {
                return (this.otherCost * 0.40) * 50;
            }
            return this.otherCost * 50;
        },

        get w2Equivalent() {
            return Math.max(0, this.netHourly / 1.25);
        },

        get requiredGrossForGoal() {
            if (this.targetWeeklyIncome <= 0 || this.gross <= 0 || this.totalNet <= 0) return 0;
            const margin = this.totalNet / this.gross;
            return margin > 0 ? this.targetWeeklyIncome / margin : 0;
        },

        get hoursToGoal() {
            const remaining = this.targetWeeklyIncome - this.totalNet;
            if (remaining <= 0) return 0;
            if (this.netHourly <= 0) return 999;
            return remaining / this.netHourly;
        },

        setMode(mode) {
            this.calculationMode = mode;
            if (mode === 'standard') {
                this.selectedVehicleId = 'irs-standard';
            } else if (this.selectedVehicleId === 'irs-standard') {
                this.selectedVehicleId = initialData.defaultVehicle || 'prius';
            }
        },

        get viralText() {
            return `My real hourly wage on ${this.selectedApp} is $${this.netHourly.toFixed(2)}/hr after vehicle cost and tax reserve. Calculate yours at ${window.location.origin}`;
        },

        shareResult() {
            if (navigator.share) {
                navigator.share({
                    title: 'My Real Gig Wage',
                    text: this.viralText,
                    url: window.location.origin
                }).catch((err) => {
                    console.log('Native share dismissed or failed', err);
                    this.showShareModal = true;
                });
                return;
            }

            this.showShareModal = true;
        },

        shareTo(platform) {
            const text = encodeURIComponent(this.viralText);
            const url = encodeURIComponent(window.location.origin);
            let shareUrl = '';

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
                        navigator.clipboard.writeText(`${this.viralText} ${window.location.href}`)
                            .then(() => alert('Copied to clipboard.'));
                    }
                    return;
                default:
                    return;
            }

            window.open(shareUrl, '_blank', 'width=600,height=400');
        },

        tweetResult() {
            this.shareTo('twitter');
        },

        saveScenario() {
            if (this.gross <= 0 || this.hours <= 0) {
                alert('Please enter hours and earnings first.');
                return;
            }

            const stamp = new Date().toLocaleDateString();
            const scenario = {
                id: Date.now(),
                date: stamp,
                app: this.selectedApp,
                gross: this.gross,
                hours: this.hours,
                netHourly: this.netHourly.toFixed(2),
                urlParams: this.buildScenarioUrl(`/${this.selectedApp.toLowerCase()}`)
            };

            this.savedScenarios.unshift(scenario);
            if (this.savedScenarios.length > 5) {
                this.savedScenarios.pop();
            }

            localStorage.setItem('gigwager_saved_scenarios', JSON.stringify(this.savedScenarios));
            alert('Scenario saved. You can compare it later.');
        },

        clearScenarios() {
            this.savedScenarios = [];
            localStorage.removeItem('gigwager_saved_scenarios');
        }
    };
};
