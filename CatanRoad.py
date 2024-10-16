import sys
from CatanNode import *

class CatanRoad(object):
    """ This class contains a road that connects two nodes """
    def __init__(self,sourceNode=None,destinationNode=None,roadBuilt:bool=False,value:int=1):
        self._start = sourceNode
        self._end = destinationNode
        self._built = roadBuilt
        self._value = value
        self._name = "Road"

    def __repr__(self):
        return(f"CatanRoad from: {self._start} to: {self._end} built: {self._built}")

    def __str__(self):
        return(f"CatanRoad from: {self._start} to: {self._end}")
    
    @property
    def value(self):
        return(1)
    
    def get_end(self):
        return self._end

def main():
    temp=CatanRoad()
    print(temp)
    print(temp.__repr__())
    print(temp.get_end())
    node1 = CatanNode("Node1")
    node2 = CatanNode("Node2")
    temp=CatanRoad(node1,node2,True)
    print(temp)

if __name__ == "__main__":
    sys.exit(int(main() or 0))