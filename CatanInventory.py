import sys
from CatanDice import catanFaces
from DiceList import *

class CatanInventory(object):
    """description of class"""
    def __init__(self):
       self._inventory={}
        # create an empty dictionary of items
       for number, name in catanFaces.items():
           self._inventory[name] = 0       
    
    def __repr__(self):
        return(f"CatanInventory: {self._inventory}")

    def __str__(self):
        tempStr = ""
        for key,value in self._inventory.items():
            tempStr = tempStr + key + ": " + str(value) + " "
        return tempStr

    def resourceCount(self,resourceName):
        count = self._inventory.get(resourceName,-1)
        if count<0:
            return(0)
        else:
            return(count)

    def incrementResource(self,resourceName):
        count = self._inventory.get(resourceName,-1)
        if count<0:
            #invalid resource
            ex=ValueError()
            ex.strerror = "Value must be a valid resource."
            raise ex
        else:
            self._inventory[resourceName] += 1

    def incrementResourceList(self,resourceList):
        for resource in resourceList:
            self.incrementResource(resource)

    def addDiceList(self,diceList:DiceList):
        for die in diceList:
            self.incrementResource(str(die))

    def reset(self):
        self.__init__()

    def checkAgainstInventory(self,requirements):
        keys = set(requirements.keys())
        for key in keys:
            if self._inventory[key] < requirements[key]:
                return False
        return True

    def decreaseInventory(self,requirements):
        keys = set(requirements.keys())
        for key in keys:
            self._inventory[key]-= 1 #decrement the inventory

    def include(self,knightInventory):
        for key,value in self._inventory.items():
            self._inventory[key]=value + knightInventory.resourceCount(key)

def main():
    temp=CatanInventory()
    print(temp)
    temp.incrementResource("gold")
    print(temp.__repr__())
    goldValue= temp.resourceCount("gold")
    print("gold value: ",goldValue)

if __name__ == "__main__":
    sys.exit(int(main() or 0))
