const fs = require('fs');

const cityDataPath = 'src/main/java/com/gigwager/model/CityData.java';
const jsonFilePath = 'src/main/resources/data/cities_local.json';

const javaContent = fs.readFileSync(cityDataPath, 'utf8');

const regex = /^[ \S]+\("([a-z\-]+)",/gm;
const slugs = [];
let match;
while ((match = regex.exec(javaContent)) !== null) {
    slugs.push(match[1]);
}

const data = JSON.parse(fs.readFileSync(jsonFilePath, 'utf8'));

slugs.forEach(slug => {
    if (!data[slug]) {
        data[slug] = {
            nightlifeDistrict: "Downtown",
            shoppingDistrict: "Main Street",
            airport: "Regional Airport",
            majorHighway: "Local Highway"
        };
    }
});

fs.writeFileSync(jsonFilePath, JSON.stringify(data, null, 4), 'utf8');
console.log('Updated cities_local.json with missing cities.');
