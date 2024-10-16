"""
This objects c
"""
import sys
from CatanNode import CatanNode
from CatanRoad import CatanRoad

class CatanBoard():
    """This is the board for game 1"""
    _nodeList = []
    _roadList = []
    def __init__(self):
        """__init__ builds the board"""
        self.startNode = CatanNode("start",None,None,True,0,[1])
        self._nodeList.append(self.startNode)
        self.Node1 = CatanNode("Settlement",None,None,False,3,[1])
        self._nodeList.append(self.Node1)
        self.Road0 = CatanRoad(self.startNode,self.Node1,True,0)
        self.startNode._road1 = self.Road0
        self.Node2 = CatanNode("Node",None,None,True,0,[1,2])
        self._nodeList.append(self.Node2)
        self.Road1 = CatanRoad(self.Node1,self.Node2)
        self._roadList.append(self.Road1)
        self.Node1._road1 = self.Road1
        self.Node3 = CatanNode("City",None,None,False,7,[1,2])
        self._nodeList.append(self.Node3)
        self.Road2 = CatanRoad(self.Node2,self.Node3)
        self._roadList.append(self.Road2)
        self.Node2._road1 = self.Road2
        self.Node4 = CatanNode("Settlement",None,None,False,4,[2,3])
        self._nodeList.append(self.Node4)
        self.Road3 = CatanRoad(self.Node2,self.Node4)
        self._roadList.append(self.Road3)
        self.Node2._road2 = self.Road3
        self.Node5 = CatanNode("Node",None,None,True,0,[2,3])
        self._nodeList.append(self.Node5)
        self.Road4 = CatanRoad(self.Node4,self.Node5)
        self._roadList.append(self.Road4)
        self.Node4._road1 = self.Road4
        self.Node6 = CatanNode("City",None,None,False,12,[2])
        self._nodeList.append(self.Node6)
        self.Road5 = CatanRoad(self.Node5,self.Node6)
        self._roadList.append(self.Road5)
        self.Node5._road1 = self.Road5
        self.Node7 = CatanNode("Settlement",None,None,False,5,[3])
        self._nodeList.append(self.Node7)
        self.Road6 = CatanRoad(self.Node5,self.Node7)
        self._roadList.append(self.Road6)
        self.Node5._road2 = self.Road6
        self.Node8 = CatanNode("Node",None,None,True,0,[3])
        self._nodeList.append(self.Node8)
        self.Road7 = CatanRoad(self.Node7,self.Node8)
        self._roadList.append(self.Road7)
        self.Node7._road1 = self.Road7
        self.Node9 = CatanNode("Settlement",None,None,False,7,[3,4])
        self.Road8 = CatanRoad(self.Node8,self.Node9)
        self._roadList.append(self.Road8)
        self._nodeList.append(self.Node9)
        self.Node8._road1 = self.Road8
        #Node9 splits
        self.Node10 = CatanNode("Node",None,None,True,0,[4])
        self._nodeList.append(self.Node10)
        self.Road9 = CatanRoad(self.Node9,self.Node10)
        self._roadList.append(self.Road9)
        self.Node9._road1 = self.Road9
        self.Node11 = CatanNode("City",None,None,False,20,[4])
        self._nodeList.append(self.Node11)
        self.Road10 = CatanRoad(self.Node10,self.Node11)
        self._roadList.append(self.Road10)
        self.Node10._road1 = self.Road10
        self.Node12 = CatanNode("Node",None,None,True,0,[4,5])
        self._nodeList.append(self.Node12)
        self.Road11 = CatanRoad(self.Node11,self.Node12)
        self._roadList.append(self.Road11)
        self.Node11._road1 = self.Road11
        self.Node13 = CatanNode("City",None,None,False,30,[5])
        self._nodeList.append(self.Node13)
        self.Road16 = CatanRoad(self.Node12,self.Node13)
        self._roadList.append(self.Road16)
        self.Node12._road1 = self.Road16
        #Node 9 branch 2
        self.Node13 = CatanNode("Node",None,None,True,0,[3,4])
        self._nodeList.append(self.Node13)
        self.Road12 = CatanRoad(self.Node9,self.Node13)
        self._roadList.append(self.Road12)
        self.Node9._road2 = self.Road12
        self.Node14 = CatanNode("Settlement",None,None,False,9,[4,5])
        self._nodeList.append(self.Node14)
        self.Road13 = CatanRoad(self.Node3,self.Node14)
        self._roadList.append(self.Road13)
        self.Node13._road1 = self.Road13
        self.Node15 = CatanNode("Node",None,None,True,0,[5,6])
        self._nodeList.append(self.Node15)
        self.Road14 = CatanRoad(self.Node14,self.Node15)
        self._roadList.append(self.Road14)
        self.Node14._road1 = self.Road14
        self.Node16 = CatanNode("Settlement",None,None,False,11,[5,6])
        self._nodeList.append(self.Node16)
        self.Road15 = CatanRoad(self.Node15,self.Node16)
        self._roadList.append(self.Road15)
        self.Node15._road1 = self.Road14
        # board is created

    def get_Built(self,startNode):
        """returnBuilt method goes through the board and finds the nodes that are built"""
        builtSet = set([])
        if startNode._road1:
            if startNode._road1._built:
                nextNode = startNode._road1.get_end()
                if nextNode._built:
                    setOfBuiltNodes = self.get_Built(nextNode)
                    builtSet.update(setOfBuiltNodes)
        if startNode._road2:
            if startNode._road2._built:
                nextNode = startNode._road2.get_end()
                if nextNode._built:
                    setOfBuiltNodes = self.get_Built(nextNode)
                    builtSet.update(setOfBuiltNodes)
        builtList=list(builtSet)
        builtList.append(startNode)
        builtSet=set(builtList)
        return builtSet

    def get_CanBeBuiltNodeAndRoad(self,startNode):
        toBuildList = []
        #get the roads and Islands
        if startNode._road1:
            if startNode._road1._built:
                nextNode = startNode._road1.get_end()
                if not nextNode._built:
                    toBuildList.append(nextNode)
                else:
                    toBuild=self.get_CanBeBuiltNodeAndRoad(nextNode)
                    toBuildList += toBuild
            else:
                #add the road
                toBuildList.append(startNode._road1)
        if startNode._road2:
            if startNode._road2._built:
                nextNode = startNode._road2.get_end()
                if not nextNode._built:
                    toBuildList.append(nextNode)
                else:
                    toBuild=self.get_CanBeBuiltNodeAndRoad(nextNode)
                    toBuildList += toBuild
            else:
                #add the road
                toBuildList.append(startNode._road2)
        #if there is more than one city only keep the first one
        cityCount = 0
        for node in toBuildList:
            if node._name == "City":
                cityCount +=1
                if cityCount>1:
                    # get rid of this node from the list
                    toBuildList.remove(node)

        #print("DEBUG - " + str(toBuildList))
        return toBuildList

    def get_Start(self):
        return self.startNode

    def islandsTouched(self):
        nodeList = list(self.get_Built(self.startNode))
        islandList = []
        for node in nodeList:
            islandList.extend(node._islands)
        return list(set(islandList)) # get rid of the duplicates

    def totalPoints(self):
        count=0
        # gather up the points
        for node in self._nodeList:
            if node._built:
                count += node._value
        #gather up the road points
        for road in self._roadList:
            if road._built:
                count+=1
        return count-16

    def longestRoad(self, startNode:CatanNode):
        road1 = startNode._road1
        road2 = startNode._road2
        if not road1 and not road2: return 0
        road1Length=0
        road2Length=0
        if road1:
            if road1._built:
                road1Length = self.longestRoad(startNode._road1._end) + 1
            else: road1Length = 0
        if road2:
            if road2._built:
                road2Length = self.longestRoad(startNode._road2._end) + 1
            else: road2Length = 0
        if road1Length>=road2Length:
            return road1Length
        else: return road2Length

def main():
    """
    main only used to verify the python when run from a command prompt.

    Returns
    -------
    None.

    """
    temp=CatanBoard()
    startNode = temp.get_Start()
    buildToList = temp.get_CanBeBuiltNodeAndRoad(startNode)
    print(buildToList) #should list the first settlement as needing to be built
    nextNodeInLine = startNode._road1.get_end()
    nextNodeInLine._built = True # now we should see the roads connected to this settlement
    buildToList = temp.get_CanBeBuiltNodeAndRoad(startNode)
    print(buildToList)
    thirdNodeIneLine = nextNodeInLine._road1.get_end()
    thirdNodeIneLine._built = True #this should be the same as the one above since there is no road built to this settlement yet
    buildToList = temp.get_CanBeBuiltNodeAndRoad(startNode)
    print(buildToList)
    nextNodeInLine._road1._built=True #now we built the road so the roads beyond the node should show up
    buildToList = temp.get_CanBeBuiltNodeAndRoad(startNode)
    print(buildToList)
    print(temp.islandsTouched())
    print("") #dummy

if __name__ == "__main__":
    sys.exit(int(main() or 0))
    