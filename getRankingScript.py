#getRankingsScript.py
#By:Bryan Lorden
#08/19/2022

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time

print("Imports Done...")
 
PATH = "C:\\Users\\Bryan's PC\\Coding\\chromedriver.exe"
options = Options()
options.headless = False
driver = webdriver.Chrome(PATH,options=options)

driver.get("https://www.fantasypros.com/nfl/rankings/ppr-cheatsheets.php")
time.sleep(3)

EntirePlayerTableRows = driver.find_element(By.ID,'ranking-table').find_element(By.TAG_NAME,'tbody').find_elements(By.TAG_NAME,"tr")
print(len(EntirePlayerTableRows))

PlayerTableRowsOnlyNoTiers = []
for i in range(len(EntirePlayerTableRows)):
    currRow = EntirePlayerTableRows[i]
    currRowClass = currRow.get_attribute('class')
    if currRowClass == "player-row":
       PlayerTableRowsOnlyNoTiers.append(currRow)
print(len(PlayerTableRowsOnlyNoTiers))

colForPlayerRank = 0
colForPlayerName = 2
colForPlayerPosRank = 3
colForPlayerBye = 4
for i in range(len(PlayerTableRowsOnlyNoTiers)):
    currPlayerElement = PlayerTableRowsOnlyNoTiers[i]
    currPlayerStatsElements = currPlayerElement.find_elements(By.TAG_NAME,'td')

    currPlayerRank = currPlayerStatsElements[colForPlayerRank].text
    currPlayerName = currPlayerStatsElements[colForPlayerName].find_element(By.TAG_NAME, 'div').find_element(By.TAG_NAME,'a').text
    currPlayerPosRank = currPlayerStatsElements[colForPlayerPosRank].text
    currPlayerPos = currPlayerPosRank[0:2]
    currPlayerBye = currPlayerStatsElements[colForPlayerBye].text
    print("Rank " + currPlayerRank + " - " + currPlayerName + " Pos: " + currPlayerPos +
         " PosRank: " + currPlayerPosRank + " Bye Week: " + currPlayerBye)


driver.close()
driver.quit()
