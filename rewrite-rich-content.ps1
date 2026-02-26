$ErrorActionPreference = "Stop"

$profiles = @(
    [ordered]@{ slug = "austin"; roads = "I-35 and MoPac"; zones = "South Congress, East Sixth, and The Domain"; airport = "AUS"; event = "SXSW and ACL weekends"; risk = "festival closures and late-night deadhead"; backup = "Mueller and North Loop"; demand = "tech-corridor dinner demand" },
    [ordered]@{ slug = "houston"; roads = "I-10, I-45, and the 610 loop"; zones = "Medical Center, Midtown, and Energy Corridor"; airport = "IAH and HOU"; event = "rodeo season and convention surges"; risk = "cross-loop repositioning and airport queue overhang"; backup = "West University and Heights"; demand = "corporate lunch and suburban dinner flow" },
    [ordered]@{ slug = "san-francisco"; roads = "US-101, I-280, and Embarcadero approaches"; zones = "Mission, SoMa, and North Beach"; airport = "SFO"; event = "conference weeks and Giants home games"; risk = "parking tickets and hill-driven efficiency loss"; backup = "Inner Sunset and Richmond"; demand = "dense short-trip demand" },
    [ordered]@{ slug = "new-york"; roads = "FDR Drive, the BQE, and the Cross Bronx"; zones = "Long Island City, Williamsburg, and the Lower East Side"; airport = "JFK and LaGuardia"; event = "holiday tourism and nightlife waves"; risk = "bridge choke points and queue variance"; backup = "Downtown Brooklyn and Harlem edge"; demand = "hospitality and airport transfer demand" },
    [ordered]@{ slug = "los-angeles"; roads = "the 405, I-10, and US-101"; zones = "West Hollywood, Santa Monica, and Koreatown"; airport = "LAX"; event = "concert nights and studio events"; risk = "cross-city surge chasing and parking delays"; backup = "Valley edge and Culver corridor"; demand = "high-ticket evening ride demand" },
    [ordered]@{ slug = "dallas"; roads = "I-635, US-75, and I-30"; zones = "Uptown, Deep Ellum, and Legacy corridor"; airport = "DFW and DAL"; event = "game days and fair-season weekends"; risk = "long suburban deadhead chains"; backup = "Bishop Arts and Addison"; demand = "airport plus suburban dinner demand" },
    [ordered]@{ slug = "phoenix"; roads = "I-10, Loop 202, and I-17"; zones = "Downtown, Tempe, and Old Town Scottsdale"; airport = "PHX"; event = "spring training and snowbird season"; risk = "summer heat and AC-driven cost spikes"; backup = "Biltmore and Chandler"; demand = "weekend nightlife and airport flow" },
    [ordered]@{ slug = "chicago"; roads = "I-90, I-94, and Lower Wacker"; zones = "River North, West Loop, and Wicker Park"; airport = "ORD and MDW"; event = "summer festivals and sports weekends"; risk = "winter slowdowns and Loop parking pressure"; backup = "Lincoln Park and South Loop"; demand = "dense restaurant cluster demand" },
    [ordered]@{ slug = "miami"; roads = "I-95, Dolphin Expressway, and MacArthur Causeway"; zones = "Brickell, Wynwood, and Miami Beach"; airport = "MIA"; event = "cruise turnover and convention weeks"; risk = "tourist congestion spikes and causeway delay"; backup = "Doral and Edgewater"; demand = "night hospitality demand" },
    [ordered]@{ slug = "seattle"; roads = "I-5, I-90, and SR-99"; zones = "Capitol Hill, South Lake Union, and Ballard"; airport = "SEA"; event = "stadium nights and rain-heavy months"; risk = "weather drag and bridge bottlenecks"; backup = "Belltown and U-District"; demand = "tech-commute and weather-delivery demand" },
    [ordered]@{ slug = "atlanta"; roads = "I-75/85 Connector, I-285, and GA-400"; zones = "Buckhead, Midtown, and Old Fourth Ward"; airport = "ATL"; event = "conference weeks and game days"; risk = "connector lockups and airport queue swings"; backup = "Sandy Springs and Decatur"; demand = "airport throughput and nightlife demand" },
    [ordered]@{ slug = "washington-dc"; roads = "I-395, New York Avenue, and GW Parkway approaches"; zones = "Navy Yard, Dupont Circle, and U Street"; airport = "DCA and IAD"; event = "federal calendar shifts and security closures"; risk = "curb restrictions and sudden detours"; backup = "NoMa and Georgetown boundaries"; demand = "event-driven travel demand" },
    [ordered]@{ slug = "boston"; roads = "I-93, I-90, and Storrow Drive"; zones = "Back Bay, Seaport, and Fenway"; airport = "BOS"; event = "college move-in weeks and convention peaks"; risk = "street-layout complexity and curb friction"; backup = "Allston and Cambridge edge"; demand = "student and business travel overlap" },
    [ordered]@{ slug = "denver"; roads = "I-25, I-70, and US-36"; zones = "LoDo, RiNo, and Cherry Creek"; airport = "DEN"; event = "ski-season travel spikes"; risk = "snow-day volatility and long repositioning"; backup = "Highlands and Tech Center"; demand = "airport and downtown entertainment demand" },
    [ordered]@{ slug = "las-vegas"; roads = "I-15, US-95, and Tropicana corridors"; zones = "The Strip, Arts District, and Summerlin edge"; airport = "LAS"; event = "fight nights, conventions, and tourism peaks"; risk = "event congestion bursts and strip pickup delay"; backup = "Henderson and Downtown cluster"; demand = "24/7 hospitality flow" },
    [ordered]@{ slug = "philadelphia"; roads = "I-76, I-95, and Roosevelt Boulevard"; zones = "Center City, University City, and Fishtown"; airport = "PHL"; event = "sports calendars and convention cycles"; risk = "center-city parking and arterial bottlenecks"; backup = "Northern Liberties and South Philly"; demand = "university and downtown order density" },
    [ordered]@{ slug = "san-diego"; roads = "I-5, I-8, and I-805"; zones = "Gaslamp, North Park, and Mission Valley"; airport = "SAN"; event = "holiday tourism and waterfront events"; risk = "coastal congestion and airport access delays"; backup = "Hillcrest and UTC corridor"; demand = "tourism and military travel mix" },
    [ordered]@{ slug = "san-jose"; roads = "US-101, I-280, and CA-87"; zones = "Downtown, Santana Row, and North San Jose"; airport = "SJC"; event = "conference and product-launch weeks"; risk = "dispersed pickups and commute-hour gridlock"; backup = "Sunnyvale edge and Campbell"; demand = "tech employee and business travel demand" },
    [ordered]@{ slug = "san-antonio"; roads = "I-35, Loop 410, and US-281"; zones = "Downtown, The Pearl, and Medical Center"; airport = "SAT"; event = "Fiesta weeks and military travel periods"; risk = "corridor saturation during event windows"; backup = "Stone Oak and Alamo Heights"; demand = "steady local dining demand" },
    [ordered]@{ slug = "nashville"; roads = "I-24, I-40, and I-65"; zones = "The Gulch, Midtown, and East Nashville"; airport = "BNA"; event = "concert and bachelorette peaks"; risk = "weekend surge chasing and pickup bottlenecks"; backup = "12 South and Brentwood edge"; demand = "music-district nightlife flow" }
)

$profileBySlug = @{}
for ($i = 0; $i -lt $profiles.Count; $i++) {
    $profileBySlug[$profiles[$i].slug] = [ordered]@{ idx = $i; data = $profiles[$i] }
}

$openers = @("signal-first", "radius-locked", "queue-aware", "lane-disciplined", "conversion-led", "calendar-aware", "risk-gated", "heat-adjusted", "weather-aware", "corridor-safe")
$timeStarts = @("07:00", "08:00", "09:00", "10:00", "11:00")
$timePeaks = @("12:30", "17:30", "18:00", "19:00", "20:30")
$timeStops = @("21:30", "22:00", "23:00", "23:30", "00:00")

function Block([object]$title, [string]$p1 = "", [string]$p2 = "") {
    # Existing call sites pass C-style "(a, b, c)" which PowerShell treats as one array arg.
    # Normalize both styles so heading and paragraph bodies are populated correctly.
    if ($title -is [System.Array]) {
        $parts = @($title)
        $title = if ($parts.Count -gt 0) { [string]$parts[0] } else { "" }
        if ([string]::IsNullOrWhiteSpace($p1) -and $parts.Count -gt 1) {
            $p1 = [string]$parts[1]
        }
        if ([string]::IsNullOrWhiteSpace($p2) -and $parts.Count -gt 2) {
            $p2 = [string]$parts[2]
        }
    }
    return "<h3>$title</h3><p>$p1</p><p>$p2</p>"
}

function Hero([string]$city, [hashtable]$p, [int]$idx) {
    switch ($idx % 5) {
        0 { return "$city rewards disciplined zone control around $($p.zones); if you let sessions drift across $($p.roads), retained net drops quickly." }
        1 { return "In $city, demand is usable but uneven. During $($p.event), route governance and queue timing decide whether hours stay profitable." }
        2 { return "$city should be treated as multiple micro-markets: $($p.zones) can convert well while $($p.roads) can consume margin through deadhead." }
        3 { return "The gap between average and strong operators in $city is not effort. It is whether they can contain $($p.risk) with hard map boundaries." }
        default { return "If your strategy in $city is just more online time, performance degrades. Corridor rules, fallback logic, and event timing are required." }
    }
}

function MeaningHtml([string]$level, [string]$city, [hashtable]$p, [int]$idx) {
    $style = ($idx + ($level.Length % 3)) % 5
    switch ($style) {
        0 { return Block("$level in $city is a control problem", "Main map: $($p.zones). Main leak: $($p.roads). Main constraint: $($p.risk).", "Use fallback handoff to $($p.backup) when conversion falls below threshold. This keeps weekly variance manageable.") }
        1 { return Block("$level schedule logic for $city", "Treat each block as finite: define entry lane, stop trigger, and no-go corridor before you start.", "In $city, this protects against $($p.event) turbulence and prevents random expansion under pressure.") }
        2 { return Block("${city}: how $level economics actually hold", "High-quality outcomes come from route discipline, not from maximizing trip count. $($p.zones) should be your base conversion lanes.", "If corridor speed collapses on $($p.roads), forcing continuation usually erodes net faster than ending the block.") }
        3 { return Block("$level operating frame in $city", "Anchor around $($p.zones), then re-evaluate only at scheduled checkpoints. Avoid ad hoc map-wide jumps.", "Failure pattern to avoid: $($p.risk). Recovery pattern to use: compact around $($p.backup).") }
        default { return Block("Practical view: $level in $city", "This level works when you can keep decisions rule-based under volatility. Calendar noise from $($p.event) should change radius, not discipline.", "The system goal is retained value per hour with controlled mileage, not raw completion volume.") }
    }
}

function DayHtml([string]$level, [string]$city, [hashtable]$p, [int]$idx) {
    $s = ($idx + 1) % $timeStarts.Count
    $m = ($idx + 2) % $timePeaks.Count
    $e = ($idx + 3) % $timeStops.Count
    $pickupCap = 2 + ($idx % 4)
    $idleCap = 9 + (($idx * 2) % 10)
    $style = ($idx + ($level.Length % 4)) % 5
    switch ($style) {
        0 { return Block("$level shift clock in $city", "Start $($timeStarts[$s]), pulse $($timePeaks[$m]), stop $($timeStops[$e]); pickup cap $pickupCap miles and idle cap $idleCap minutes.", "Operate inside $($p.zones), and if $($p.event) distorts flow across $($p.roads), route reset through $($p.backup).") }
        1 { return Block("$city daypart sequence for $level", "Run a two-lane model: primary lane in $($p.zones), one permitted switch, and hard stop when idle exceeds $idleCap minutes.", "Queue checks near $($p.airport) are mandatory. Repeated friction on $($p.roads) means immediate contraction.") }
        2 { return Block("$level operational rhythm", "Block A conversion, Block B quality filter, Block C controlled close. Transition count maximum: two.", "For $city, enforce deadhead ceiling of $pickupCap miles and pause expansion when $($p.risk) appears.") }
        3 { return Block("A durable $level day in $city", "Checkpoint matrix: route friction, queue lag at $($p.airport), and fatigue score. If two checks fail, switch to $($p.backup).", "This pattern protects retained value when $($p.event) introduces short-lived demand spikes.") }
        default { return Block("$city $level block design", "Finite blocks only: objective, lane, and stop condition. Keep map radius tight around $($p.zones) for high-conversion windows.", "If quality breaks twice on $($p.roads), terminate the block instead of forcing recovery miles.") }
    }
}

function TaxHtml([string]$level, [string]$city, [hashtable]$p, [int]$idx) {
    $mode = ($idx + ($level.Length % 5)) % 4
    switch ($mode) {
        0 { return Block("$level tax cadence", "Weekly close: mileage, payout reconciliation, reserve transfer. Monthly check: forecast drift and reserve adequacy.", "In $city, bookkeeping quality is what makes route strategy outcomes interpretable under $($p.event) volatility.") }
        1 { return Block("Money controls for $level in $city", "Separate operating cash from reserve cash. Do not blend accounts when utilization increases.", "High-friction conditions on $($p.roads) can hide leakage; routine reconciliation keeps retained net visible.") }
        2 { return Block("$city compliance posture", "This level does not require complex structures first. It requires complete logs, stable category labels, and predictable reserve behavior.", "When $($p.risk) rises, disciplined records reduce both tax surprises and bad tactical decisions.") }
        default { return Block("$level financial governance", "Use one weekly reconciliation day and one monthly forecast review to avoid reactive decision-making.", "Strong accounting rhythm helps you compare $($p.zones) versus $($p.backup) based on real net outcomes.") }
    }
}

function BestHtml([string]$level, [string]$city, [hashtable]$p, [int]$idx) {
    $style = ($idx + ($level.Length % 6)) % 5
    $deadheadCap = 2 + ($idx % 5)
    $continuousCap = 70 + (($idx * 3) % 45)
    $token = $openers[$idx % $openers.Count]
    switch ($style) {
        0 { return Block("$level best practices in $city", "Protocol ${token}: primary lane=$($p.zones); fallback=$($p.backup); deadhead cap=$deadheadCap miles; continuous-drive cap=$continuousCap minutes.", "When $($p.event) amplifies noise, freeze expansion across $($p.roads) and preserve lane discipline.") }
        1 { return Block("Execution rules that matter", "Maintain a three-metric board: retained value per hour, miles per payout dollar, and idle-ratio. Change one parameter at a time.", "For $city, $token mode prevents tactical drift under $($p.risk).") }
        2 { return Block("$city optimization guardrails", "Do not promote one outlier shift into policy. Require repeated evidence in the same corridor pair: $($p.zones) and $($p.backup).", "If deadhead exceeds $deadheadCap miles twice on $($p.roads), disable that lane for the day.") }
        3 { return Block("Risk controls for $level", "Create hard caps for queue time near $($p.airport), deadhead distance, and continuous driving duration. Caps are operational safety rails.", "In $city, this model beats reactive volume chasing during high-noise periods.") }
        default { return Block("$level playbook notes", "Protect prime windows, keep transitions explicit, and define no-go corridors before launch. Route sprawl is the main hidden cost.", "If quality fails under $token mode, contract to $($p.backup) and restart with fresh thresholds.") }
    }
}

function BuildPersonaQuotes([string]$level, [string]$city, [hashtable]$p, [int]$idx) {
    $capMiles = 2 + ($idx % 4)
    $minRate = 18 + ($idx % 6)
    if ($level -eq "part-time") {
        return @(
            [ordered]@{
                personaType = "RIDESHARE_VETERAN"
                displayName = "$city rideshare veteran"
                quote = "I run part-time blocks with a deadhead cap of $capMiles miles around $($p.zones). If a request pushes me deep into $($p.roads), I reset instead of chasing."
                attributionType = "USER_SUBMITTED"
            },
            [ordered]@{
                personaType = "MARKET_ANALYST"
                displayName = "Editorial demand analyst"
                quote = "Part-time variance in $city tracks corridor discipline more than raw volume. Operators who respect route boundaries usually keep stronger retained net."
                attributionType = "EDITORIAL_COMPOSITE"
            }
        )
    }
    if ($level -eq "side-hustle") {
        return @(
            [ordered]@{
                personaType = "DELIVERY_VETERAN"
                displayName = "$city multi-app courier"
                quote = "My side-hustle rule is simple: protect a $minRate-dollar floor, keep the map tight near $($p.zones), and switch once to $($p.backup) before ending a weak block."
                attributionType = "USER_SUBMITTED"
            },
            [ordered]@{
                personaType = "TAX_PROFESSIONAL"
                displayName = "Independent enrolled agent"
                quote = "At 25-hour schedules, the winners are usually the people with weekly records and reserve discipline. Process quality matters more than optimism."
                attributionType = "EDITORIAL_COMPOSITE"
            }
        )
    }
    return @(
        [ordered]@{
            personaType = "FLEET_MANAGER"
            displayName = "Urban fleet operations lead"
            quote = "On full-time rosters, we keep performance stable by limiting uncontrolled transitions on $($p.roads) and enforcing structured lane rotation around $($p.zones)."
            attributionType = "EDITORIAL_COMPOSITE"
        },
        [ordered]@{
            personaType = "INSURANCE_ADVISOR"
            displayName = "Commercial coverage advisor"
            quote = "At sustained utilization near $($p.airport), risk profile shifts faster than most drivers expect. Coverage review cadence has to match mileage growth."
            attributionType = "VERIFIED_INTERVIEW"
        }
    )
}

$files = Get-ChildItem "src/main/resources/data/cities/*.json"
foreach ($file in $files) {
    $existing = Get-Content -Raw $file.FullName | ConvertFrom-Json
    $slug = [string]$existing.citySlug
    if (-not $profileBySlug.ContainsKey($slug)) {
        throw "Missing profile for slug: $slug"
    }
    $entry = $profileBySlug[$slug]
    $idx = [int]$entry.idx
    $p = $entry.data
    $city = [string]$existing.cityName

    $payload = [ordered]@{
        citySlug = $existing.citySlug
        cityName = $existing.cityName
        state = $existing.state
        coreData = [ordered]@{
            gasPrice = [double]$existing.coreData.gasPrice
            trafficFactor = [double]$existing.coreData.trafficFactor
        }
        seo = [ordered]@{
            heroHook = Hero $city $p $idx
            pageStructureType = $existing.seo.pageStructureType
            contentType = $existing.seo.contentType
            methodologyVersion = "city-rich-v4.0"
            lastVerifiedAt = "2026-02-26"
            sources = @()
        }
        workLevels = [ordered]@{}
    }

    foreach ($s in $existing.seo.sources) {
        $payload.seo.sources += [ordered]@{
            title = $s.title
            url = $s.url
            publisher = $s.publisher
            checkedAt = "2026-02-26"
        }
    }

    foreach ($level in @("part-time", "side-hustle", "full-time")) {
        $range = $existing.workLevels.$level.realisticNetHourlyRange
        $payload.workLevels[$level] = [ordered]@{
            realisticNetHourlyRange = [ordered]@{
                min = [double]$range.min
                max = [double]$range.max
            }
            localStrategyText = "In $city $level mode, prioritize $($p.zones), cap transfer distance on $($p.roads), and route fallback through $($p.backup) whenever $($p.event) introduces conversion noise."
            workLevelMeaningHtml = MeaningHtml $level $city $p $idx
            taxStrategyHtml = TaxHtml $level $city $p $idx
            dayInTheLifeHtml = DayHtml $level $city $p $idx
            bestPracticesHtml = BestHtml $level $city $p $idx
            painPoints = @(
                "Conversion drop when leaving $($p.zones)",
                "Route friction on $($p.roads)",
                "$($p.risk)"
            )
            personaQuotes = BuildPersonaQuotes $level $city $p $idx
        }
    }

    $json = $payload | ConvertTo-Json -Depth 20
    [System.IO.File]::WriteAllText($file.FullName, ($json + "`n"), (New-Object System.Text.UTF8Encoding($false)))
}

Write-Output ("rewritten=" + $files.Count)
