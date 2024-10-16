import sys
from CatanDice import catanFaces
from CatanInventory import *
from DiceList import *

class CatanKnight(list):
    """Class of the set of possible knights"""
    """each element contains a statement for if it has been built, what type of resource it provides and if it has been used"""
    def __init__(self):
        self.append([False,catanFaces[5],False])
        self.append([False,catanFaces[2],False])
        self.append([False,catanFaces[4],False])
        self.append([False,catanFaces[1],False])
        self.append([False,catanFaces[3],False])
        self.append([False,catanFaces[0],False])

    def knightInventoryString(self):
        reportString = ""
        for knightIndex in range(0,6):
            knightData = self[knightIndex]
            if knightData[0] and not knightData[2]:
                if len(reportString)>0: reportString += ", " 
                reportString += knightData[1]
        return reportString

    def nextAvailable(self,listOfIslandsTouched):
        """ returns the index in the island list of the next knight that can be built or -1 if none available """
        for knightIndex in range(0,6):
            knightData = self[knightIndex]
            if not knightData[0]: #looks like it is available to be built
                knightOnIsland = knightIndex+1
                if knightOnIsland in listOfIslandsTouched:
                    return knightIndex
                else:
                    return -1
        return -1

    def buildAKnight(self,island):
        islandToBuildKnight = self[island]
        islandToBuildKnight[0]=True
        resourceGenerated = islandToBuildKnight[1]

    def nextResource(self,listOfIslandsTouched):
        nextIsland = self.nextAvailable(listOfIslandsTouched)
        if nextIsland<0:
            return ""
        else:
            return str(self[nextIsland][1])
    
    def knightsBuiltAndAvailable(self):
        knightResourceAvailable = []
        for knight in self:
            if knight[0] and not knight[2]: # we have built it and not used it
                knightResourceAvailable.append(knight[1])
        return knightResourceAvailable
    
    def consumeKnightResource(self,resourceName):
        for knight in self:
            if knight[1] == resourceName:
                knight[2] = True
                break

    def knightInventory(self):
        _knightInventory = CatanInventory()
        for knightData in self:
            if knightData[0]==True and knightData[2]==False: #this island has been built but  not consumed
                foundResource = knightData[1]
                _knightInventory.incrementResource(foundResource)
        return _knightInventory

    def countOfKnightsBuilt(self):
        knightCount = 0
        for knight in self:
            if knight[0]: knightCount += 1
        return knightCount

def main():
        knightData = CatanKnight()
        myDice = DiceList()
        tempInventory = CatanInventory()
        #let's make sure we can build a knight
        myDice[0]._value = 2
        myDice[1]._value = 4
        myDice[2]._value = 5
        tempInventory.addDiceList(myDice)
        islandNumber = knightData.nextAvailable([1,2])
        print("Building the knight will create a :",knightData.nextResource([1,2]))
        print("Preknight inventory: " + str(tempInventory))
        knightData.buyAKnight(islandNumber,tempInventory)
        print("Postknight inventory: " + str(tempInventory))
        print("next knight resource: " + knightData.nextResource([1,2]))
        print()

if __name__ == "__main__":
    sys.exit(int(main() or 0))