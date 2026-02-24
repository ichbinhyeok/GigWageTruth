@param String app
@param String appName
@param int hoursPerWeek

<div class="bg-amber-50 rounded-2xl p-6 border-l-4 border-amber-500 mb-8 mt-4 shadow-sm">
    <h3 class="text-lg font-bold text-slate-900 mb-2 flex items-center gap-2">
        <span>💡</span> Personalized Next Step
    </h3>
    <p class="text-slate-700 text-sm mb-4">
        @if(hoursPerWeek >= 40)
            <strong>High Hours Risk:</strong> Driving ${hoursPerWeek} hours a week exposes you to higher self-employment tax liabilities. 
            <a href="/taxes/quarterly-estimator" class="text-amber-700 font-bold hover:underline">Check your quarterly tax risk &rarr;</a>
            <br>Also, consider the <a href="/insurance" class="text-amber-700 font-bold hover:underline">rideshare insurance coverage gap &rarr;</a>.
        @elseif(hoursPerWeek >= 15)
            <strong>High Mileage Risk:</strong> Based on an average ${hoursPerWeek} hours per week, your car takes a beating.  
            <a href="/vehicle-cost/cost-per-mile" class="text-amber-700 font-bold hover:underline">Check your true cost per mile &rarr;</a>
        @else
            <strong>Maximize Your Time:</strong> Want to earn more in fewer hours? 
            <a href="/multi-apping" class="text-amber-700 font-bold hover:underline">Read the multi-apping strategy guide &rarr;</a>
        @endif
    </p>
</div>
