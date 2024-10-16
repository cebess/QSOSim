import sys
from CatanDice import catanFaces

class CatanIsland(object):
    """This class is for the islands of Catan"""
    def __init__(self,islandInt=None,resource=None,built=False):
        self._resource = resource
        self._built = built
        self._islandInt = islandInt
    
    def __repr__(self):
        return(f"Island {self._islandInt} containing: {self.islandDesc()}")
    
    def islandDesc(self):
        return catanFaces[self._resource]
 
class CatanIslandChain(list):
    def __init__(self):
        island1 = CatanIsland(1,5) #Ore
        self.append(island1)
        island2 = CatanIsland(2,2) #Wheat
        self.append(island2)
        island3 = CatanIsland(3,4) #Sheep
        self.append(island3)
        island4 = CatanIsland(4,1) #Wood
        self.append(island4)
        island5 = CatanIsland(5,3) #Brick
        self.append(island5)
        island6 = CatanIsland(6,0) #Gold
        self.append(island6)
   

def main():
    temp=CatanIslandChain()
    print(temp[0])
    print("done")

if __name__ == "__main__":
    sys.exit(int(main() or 0))
