import json
import re
import os

city_data_path = 'src/main/java/com/gigwager/model/CityData.java'
json_file_path = 'src/main/resources/data/cities_local.json'

with open(city_data_path, 'r', encoding='utf-8') as f:
    java_content = f.read()

# Extract all slugs
pattern = re.compile(r'^[ \t]+[A-Z_]+\(\"([a-z\-]+)\",', re.MULTILINE)
slugs = pattern.findall(java_content)

with open(json_file_path, 'r', encoding='utf-8') as f:
    data = json.load(f)

for slug in slugs:
    if slug not in data:
        data[slug] = {
            "nightlifeDistrict": "Downtown",
            "shoppingDistrict": "Main Street",
            "airport": "Regional Airport",
            "majorHighway": "Local Highway"
        }

with open(json_file_path, 'w', encoding='utf-8') as f:
    json.dump(data, f, indent=4)
print('Updated cities_local.json with missing cities.')
