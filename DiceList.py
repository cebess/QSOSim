import sys

from CatanDice import *

class DiceList(list):
    """Class of the set of 6 dice"""
    def __init__(self):
        for x in range(0,6):
            die = CatanDice()
            self.append(die)

    def reRoll(self,die:int=-1):
        if die<0:
            for x in range(0,6):
                self[x].roll()
        else:
            self[die].roll()
    
    def __repr__(self):
        return(f"DiceList: {list(self)}")

    def __str__(self):
        tempStr = ""
        for x in range(0,len(self)):
            tempStr += str(x+1) + ":" + str(self[x]) + " "
        return tempStr

    def dieCount(self,resourceString):
        count = 0
        for die in self:
            if str(die)==resourceString:
                count += 1
        return(count)

def main():
    myDice = DiceList()
    print(myDice)
    myDice.reRoll(1)
    print(myDice[1])
    myDice.reRoll(-1)
    print(myDice)

if __name__ == "__main__":
    sys.exit(int(main() or 0))