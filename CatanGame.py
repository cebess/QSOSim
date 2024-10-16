import sys
#import copy
#from CatanDice import CatanDice
from CatanNode import *
from CatanRoad import *
from CatanBoard import *
from DiceList import *
from CatanInventory import *
from CatanIsland import *
from CatanKnight import *
from colorama import init,Fore,Back

CatanCosts = {"Settlement":{"wood":1,"brick":1,"wheat":1,"sheep":1},
            "Road":{"wood":1,"brick":1},
            "City":{"ore":3,"wheat":2},
            "Knight":{"wheat":1,"sheep":1,"ore":1}}

def getPossible(boardInventory:CatanInventory,knightData,boardPossibilities,gameBoard):
    """
    Parameters
    ----------
    boardInventory : CatanInventory
        The board elements
    knightData : TYPE
        The knights that are defined
    boardPossibilities : TYPE
        DESCRIPTION.
    gameBoard : TYPE
        DESCRIPTION.

    Returns
    -------
    possibilities : TYPE
        The items that are positioned to be built

    """
    possible = []
    myInventory = {}
    knightInventory = knightData.knightInventory()
    for key,value in boardInventory._inventory.items():
        myInventory[key]=value + knightInventory.resourceCount(key)
    # we should not have a total inventory
    for objectToBuild,resource in CatanCosts.items():
        if covered(myInventory,resource):
            possible.append(objectToBuild)
    #validate resources possibilities against board and knight possibilities
    possibilities = []
    if len(possible)>0:
        if "Knight" in possible:
            #check to make sure make sure we have an island with an unbuilt knight
            islandNumber = knightData.nextAvailable(gameBoard.islandsTouched())
            if islandNumber>=0:
                possibilities.append("Knight")
        for boardItem in boardPossibilities:
            typeName = str(type(boardItem))
            if "Node" in typeName:
                nodeType = boardItem._name
                if nodeType in possible:
                    possibilities.append(boardItem)
            elif "Road" in typeName:
                if "Road" in possible:
                    possibilities.append(boardItem)
            else:
                possibilities.append("Unknown")
    return possibilities

def covered(myInventory:set,resourcesRequired:set):
    for key,value in resourcesRequired.items():
        if value>myInventory[key]:
            return False
    return True

def removeResourcesFromInventory(boardInventory:CatanInventory,knightData:CatanKnight,resourceBuiltName:str):
    resourcesRequired = CatanCosts[resourceBuiltName]
    for need in resourcesRequired:
        count = boardInventory.resourceCount(need)
        if count>=1:
            #need addressed by Inventory
            boardInventory._inventory[need]-=1
            next
        else:
        #looks like we need to look for one of the knights
            knightData.consumeKnightResource(need)
            next

def resourceRequiredString(resourceNeed:str):
    tempString = ""
    listOfResources = CatanCosts[resourceNeed]
    for item,value in listOfResources.items():
        tempString += ", " + str(value) + " " + item
    return tempString[2:]


def reportPossibleMoves(gameBoard,knightData,turnDice):
    """ getPossibleMovesStrings - reports out the dice required to build something
    """
    startNode = gameBoard.get_Start()
    tempInventory = CatanInventory()
    tempInventory.addDiceList(turnDice)
    tempList = gameBoard.get_CanBeBuiltNodeAndRoad(startNode)
    # for each of the items in the tempList find out what it would take in addition to the current dice
    print("You may be able to build:")
    for item in tempList:
        print(item._name + " requiring: " + resourceRequiredString(item._name))
    tempIsland = knightData.nextAvailable(gameBoard.islandsTouched())
    if tempIsland>=0:
        print("Knight on island " + str(tempIsland+1) + ": requiring: 1 ore, 1 wheat, 1 sheep")
    return "" 

def main():
    """
    Main module to run the Catan Dice Game from the command line

    Returns
    -------
    None.

    """
    numberOfTurns=15
    init(autoreset=True)
    gameBoard = CatanBoard()
    gameInventory = CatanInventory()
    #gameIslandChain = CatanIslandChain()
    knightData = CatanKnight()
    #now all the parts should be initialized
    startNode = gameBoard.get_Start()
    turn=1
    while (True):
        # add the dice to my inventory
        turnDice = DiceList()
        rollingCount = 0
        print(Fore.LIGHTYELLOW_EX + "Turn: "+ str(turn))
        reportPossibleMoves(gameBoard,knightData,turnDice)
        tempString = knightData.knightInventoryString()

        if len(tempString)>0:
            print("Knight resources: " + tempString)
        print("You rolled:")
        while rollingCount<2: # you get the inital roll plus two extra
            # do a roll
            try:
                print(str(turnDice))
                reRollList = [int(x) for x in input(f"roll: {rollingCount+1} - Which dice would you like to reroll? (comma separated list or zero to accept them all): ").split(',')]
                if reRollList == [0]:
                    rollingCount = 3
                else:
                    # get rid of duplicates
                    reRollList = list(set(reRollList))
                    if len(reRollList)>len(turnDice): # they can only reroll the number of dice that are in the turnDice list
                        print(Fore.RED + Back.WHITE + "You must enter less than 6 dice in your list")
                    else: # make sure the numbers are valid
                        for dieNumber in reRollList:
                            dieOK = True
                            if dieNumber<1 or dieNumber>6:
                                print(Fore.RED + Back.WHITE + "The number of the dice must be between 1 and 6")
                                dieOK = False
                                break
                        # it looks like we can now reroll the dice
                        if dieOK:
                            for dieNumber in reRollList:
                                turnDice[dieNumber-1].roll()
                            rollingCount += 1
            except ValueError as e:
                print(Fore.RED + Back.WHITE + "Please enter a comma separated list of numbers between 1 and 6. Enter zero if you want to accept them all.")
        #check to see if they have a number of gold dies
        while (turnDice.dieCount("gold")>=2):
            print()
            print(turnDice)
            print("Looks like you have at least two gold. What would you like to turn them into?")
            inputStr = input("1:wood, 2:wheat, 3:brick, 4:sheep, 5:ore: ")
            try:
                newResource = int(inputStr)
                if newResource<1 or newResource>5:
                    print(Fore.RED + Back.WHITE +"Please select a number from 1-5")
                else:
                    #Looks like we have a valid resource
                    count=2
                    while count > 0:
                        for die in turnDice:
                            if (0 == die.value): # if it is gold
                                if count>1:
                                    die._value = newResource
                                    count-=1
                                else:
                                    turnDice.remove(die)
                                    count-=1
                                    break #we are done
            except ValueError as e:
                print("Please enter a resource number from 1-5")
        #now that we have the dice nailed down, let's add it to our inventory of resources
        gameInventory.addDiceList(turnDice)
        buildToList = gameBoard.get_CanBeBuiltNodeAndRoad(startNode)
        possibleThings=getPossible(gameInventory,knightData,buildToList,gameBoard)
        also = " "
        if len(possibleThings) ==0:
            print(Fore.RED + Back.WHITE + "Looks like you cannot build anything this turn.")
        else:
            while len(possibleThings)>0:
                print("It looks like you can" + also + "build a: ")
                loop = 1
                if len(possibleThings)==1:
                    print(str(possibleThings[0]))
                    inputStr = "1"
                    #print(Fore.GREEN + "So we built the " + str(possibleThings[0]))
                else:
                    for element in possibleThings:
                        print(str(loop) + " - " + str(element))
                        loop += 1
                    inputStr = input("Which would you like to create: (hit 0 for none) ")
                try:
                    value = int(inputStr)
                    if value<0 or value>loop:
                        print(Fore.RED + Back.WHITE + "Please select a number from 1-" + str(loop))
                    else:
                        if value == 0:
                            break
                        else:
                            buildThis = possibleThings[value-1]
                            if buildThis == "Knight":
                                islandNumber = knightData.nextAvailable(gameBoard.islandsTouched()) #check against the list of touched islands

                                if islandNumber>=0:
                                    knightData.buildAKnight(islandNumber)
                                    removeResourcesFromInventory(gameInventory,knightData,"Knight")
                                    print(Fore.GREEN +Back.WHITE + "Built the knight")
                                else:
                                    print(Fore.RED + Back.WHITE + "Error - cannot build that knight yet!")
                            else:
                                print(Fore.GREEN +Back.WHITE + "Built the " + buildThis._name)
                                buildThis._built = True
                            # subtract out what we built from the inventory
                                removeResourcesFromInventory(gameInventory,knightData,buildThis._name)
                except ValueError as e:
                    print(Fore.RED + Back.WHITE + "Please enter a number from the list.")
                buildToList = gameBoard.get_CanBeBuiltNodeAndRoad(startNode)
                possibleThings=getPossible(gameInventory,knightData,buildToList,gameBoard)
                #print("Debug Board inventory: " + str(gameInventory))
                #print("Debug Knight inventory: " + str(knightData.knightInventory()))
                #print("Debug - " + str(possibleThings))
                also = " also "
        print(" ")
        gameInventory.reset()
        turn += 1
        if turn>numberOfTurns:
            break
        else:
            print("-- next turn --")
    print(Fore.GREEN + Back.WHITE+ "End of Game")
    print("Total points from board: " + str(gameBoard.totalPoints()))
    print("Total knight count: " + str(knightData.countOfKnightsBuilt()))
    print("Longest road length: "  + str(gameBoard.longestRoad(gameBoard.Node1)))

if __name__ == "__main__":
    sys.exit(int(main() or 0))

