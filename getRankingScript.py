#getRankingsScript.py
#By:Bryan Lorden
#08/19/2022

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
import time

print("Imports Done")

def setUp():
    PATH = "C:\\Users\\Bryan's PC\\Coding\\chromedriver.exe"
    options = Options()
    options.headless = False
    driver = webdriver.Chrome(PATH,options=options)
    return driver 

def getRankingsPage(webdriver):
    webdriver.get("https://www.fantasypros.com/nfl/rankings/ppr-cheatsheets.php")
    time.sleep(3)

def getCurrPlayerPPRProjections(webdriver,currPlayerProfileLink,currPlayerPosition):

    def findPPROptionFromSelecter(webdriver,scoringFormatSelecterOptionsList):
        for currFormatSelecter in scoringFormatSelecterOptionsList:
            if currFormatSelecter.text == "PPR":
                return currFormatSelecter

    def getToCurrPlayerPPRProjections(webdriver,currPlayerProjectionsPage,currPlayerPosition):
        webdriver.get(currPlayerProjectionsPage)
        time.sleep(2)


        if currPlayerPosition == "WR" or currPlayerPosition == "TE" or currPlayerPosition == "RB":
            scoringFormatSelecter = webdriver.find_element(By.CSS_SELECTOR,'#main-container > div > div.main-content > div > div:nth-child(4) > div.head-row.clearfix > div > div > select')
            scoringFormatSelecterOptionsList = scoringFormatSelecter.find_elements(By.TAG_NAME,'option')
            PPRSelecterOption = findPPROptionFromSelecter(webdriver,scoringFormatSelecterOptionsList)
            PPRSelecterOption.click()
            time.sleep(2)
    
    def findColforPointsInProjectionsTable(webdriver):
        projectionTableOfContentsCol = webdriver.find_element(By.CSS_SELECTOR,'#main-container > div > div.main-content > div > div:nth-child(3) > div.body-row > div > table > thead > tr').find_elements(By.TAG_NAME,'th')
        for currCol in projectionTableOfContentsCol:
            if currCol.text == "POINTS":
                return str(projectionTableOfContentsCol.index(currCol)+1)
    
    colForProjectionsInMainPlayerMenu = 7

    webdriver.get(currPlayerProfileLink)
    time.sleep(2)

    PlayerProfileMenuElementList = webdriver.find_element(By.CSS_SELECTOR,'#main-container > div > div.main-content > div > div.pills-wrap.feature-bg.feature-stretch').find_element(By.TAG_NAME,"ul").find_elements(By.TAG_NAME,"li")
    ProjectionsColElement = PlayerProfileMenuElementList[colForProjectionsInMainPlayerMenu]
    currPlayerProjectionsPage = ProjectionsColElement.find_element(By.TAG_NAME,'a').get_attribute('href')
    getToCurrPlayerPPRProjections(webdriver,currPlayerProjectionsPage,currPlayerPosition)

    colOfPointsInProjectionsTable = findColforPointsInProjectionsTable(webdriver)
    return webdriver.find_element(By.CSS_SELECTOR,'#main-container > div > div.main-content > div > div:nth-child(3) > div.body-row > div > table > tbody > tr > td:nth-child(' + colOfPointsInProjectionsTable + ')').text
     


def getPlayerRowsInTable(webdriver,EntirePlayerTable):
    PlayerTableRowsOnlyNoTiersList = []
    for i in range(len(EntirePlayerTable)):
        currRow = EntirePlayerTable[i]
        currRowClass = currRow.get_attribute('class')
        if currRowClass == "player-row":
            PlayerTableRowsOnlyNoTiersList.append(currRow)
    return PlayerTableRowsOnlyNoTiersList

def getPPRProjections(webdriver,allPlayersStatsDict):
    for key in allPlayersStatsDict:
        currRank = key
        currPlayerDict = allPlayersStatsDict[currRank]
        currPlayerProfileLink = currPlayerDict['Profile Link']
        currPlayerPosition = currPlayerDict['Position']
        currPlayerDict['PPR Point Projection'] = getCurrPlayerPPRProjections(webdriver,currPlayerProfileLink,currPlayerPosition)
        del currPlayerDict['Profile Link']
        print(currPlayerDict)

def sortAllPlayerStats(webdriver,PlayerTableRowsOnlyNoTiersList):
    colForPlayerRank = 0
    colForPlayerName = 2
    colForPlayerPosRank = 3
    colForPlayerBye = 4
    allPlayersStatsDict = {}

    for i in range(len(PlayerTableRowsOnlyNoTiersList)):
        currPlayerElement = PlayerTableRowsOnlyNoTiersList[i]
        currPlayerStatsElements = currPlayerElement.find_elements(By.TAG_NAME,'td')
        currPlayerRank = currPlayerStatsElements[colForPlayerRank].text
        currPlayerName = currPlayerStatsElements[colForPlayerName].find_element(By.TAG_NAME, 'div').find_element(By.TAG_NAME,'a').text
        currPlayerProfileLink = currPlayerStatsElements[colForPlayerName].find_element(By.TAG_NAME, 'div').find_element(By.TAG_NAME,'a').get_attribute("href")
        currPlayerPosRank = currPlayerStatsElements[colForPlayerPosRank].text
        currPlayerPos = currPlayerPosRank[0:2]
        currPlayerBye = currPlayerStatsElements[colForPlayerBye].text

        currPlayerDictSoFar = {'Rank' : currPlayerRank, 'Name' : currPlayerName, 'Position'\
            : currPlayerPos, 'Position Rank' : currPlayerPosRank, 'Bye Week' : currPlayerBye,\
            'PPR Point Projection' : 0, 'Profile Link' : currPlayerProfileLink}

        allPlayersStatsDict[currPlayerDictSoFar['Rank']] = currPlayerDictSoFar

    getPPRProjections(webdriver,allPlayersStatsDict)
    
    return allPlayersStatsDict


def main():

    driver = setUp()
    getRankingsPage(driver)

    EntirePlayerTableRows = driver.find_element(By.ID,'ranking-table').find_element(By.TAG_NAME,'tbody').find_elements(By.TAG_NAME,"tr")
    PlayerTableRowsOnlyNoTiersList = getPlayerRowsInTable(driver,EntirePlayerTableRows)

    allPlayerStats = sortAllPlayerStats(driver,PlayerTableRowsOnlyNoTiersList)
    print(allPlayerStats)

    driver.close()
    driver.quit()

main()
