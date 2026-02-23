import os
import glob

files = [
    r"c:\Development\Owner\GigWageTruth\src\main\jte\uber.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\doordash.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\terms.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\salary\directory.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\salary\city-work-level.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\salary\city-report.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\privacy.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\pages\calculator.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\methodology.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\index.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\contact.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\about.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\blog\uber-vs-doordash.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\blog\tax-guide.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\blog\multi-apping-guide.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\blog\index.jte",
    r"c:\Development\Owner\GigWageTruth\src\main\jte\blog\hidden-costs.jte"
]

for f in files:
    with open(f, "r", encoding="utf-8") as file:
        content = file.read()
    
    lines = content.split('\n')
    insert_idx = 0
    for i, line in enumerate(lines):
        if line.startswith("@param") or line.startswith("@import"):
            insert_idx = i + 1
    
    if "@param Boolean noIndex = false" not in content:
        lines.insert(insert_idx, "@param Boolean noIndex = false")
    
    content = '\n'.join(lines)
    
    if "noIndex = noIndex," not in content:
        content = content.replace("@template.layout.main(", "@template.layout.main(\n    noIndex = noIndex,")
        
    with open(f, "w", encoding="utf-8") as file:
        file.write(content)

print("Done updating JTE files.")
