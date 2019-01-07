import math


def distance_euclidean(a, b, dim):
    distance = 0
    for i in range(dim):
        distance += pow((a[i] - b[i]), 2)
    distance = math.sqrt(distance)
    return distance

def distance_manhattan(a, b, dim):
    distance = 0
    for i in range(dim):
        distance += abs(a[i] - b[i])
    distance = math.sqrt(distance)
    return distance

def distance_hamming(a, b, dim):
    distance = 0
    for i in range(dim):
        if a[i] != b[i]:
            distance += 1
    return distance


class kNN:
    def __init__(self, trainSet, dim, k, distFunc):
        self.dimension = dim
        self.k = k
        self.trainSet = trainSet
        self.distance = distFunc

    def neighbors(self, data):
        """
        For given new data, return a list of k nearest neighbors
        :param data:
        :return:
        """
        neighbors = []
        set = self.trainSet.copy()
        set.sort(key=lambda x: self.distance(x, data, self.dimension))
        for i in range(self.k):
            neighbors.append(set[i])
        return neighbors

    def classify(self, data):
        """
        Vote to get the prediction.
        :param data:
        :return:
        """
        labels = []
        for neighbor in self.neighbors(data):
            labels.append(neighbor[self.dimension])
        return max(labels, key=labels.count)


a1 = [0,1,1,0,0,
      0,0,0,1,1,
      0,0,0,1,1,
      0,1,1,0,0,
      1,1,1,1,0]

a2 = [1,1,0,0,0,
      1,1,0,0,0,
      0,1,1,0,0,
      0,0,1,1,0,
      0,1,1,1,1]

a3 = [0,0,1,1,0,
      0,0,0,0,0,
      0,1,1,1,1,
      1,1,1,0,0,
      1,1,1,1,0]

a4 = [1,1,0,0,0,
      0,1,1,1,1,
      0,0,0,1,1,
      0,0,1,1,0,
      0,1,1,1,1]

a5 = [1,1,1,0,0,
      1,1,1,1,1,
      0,0,0,0,0,
      0,1,1,0,0,
      0,0,0,0,0]

b1 = [0,0,1,1,0,
      0,0,1,1,0,
      1,0,1,0,0,
      1,1,1,0,0,
      0,1,1,0,0]

b2 = [0,1,1,0,0,
      1,1,1,1,0,
      1,1,1,1,0,
      0,1,1,0,1,
      0,0,0,0,1]

b3 = [0,1,0,1,1,
      0,1,0,1,1,
      1,1,1,0,1,
      1,1,1,1,0,
      0,0,0,1,0]

b4 = [0,0,0,0,1,
      0,1,1,0,1,
      1,1,1,0,0,
      1,1,0,0,1,
      1,1,0,0,1]

b5 = [0,1,0,0,1,
      1,1,1,0,1,
      1,0,1,0,1,
      0,0,1,0,1,
      0,0,1,0,1]

m1 = [0,1,1,0,1,
      0,1,1,1,1,
      1,1,0,0,0,
      1,1,0,1,0,
      0,1,0,1,0]

m2 = [0,0,1,1,1,
      1,0,1,1,1,
      0,1,0,0,1,
      1,1,0,1,0,
      1,0,0,0,0]

m3 = [1,1,0,0,0,
      0,0,0,1,0,
      1,1,1,1,0,
      0,0,0,0,1,
      1,1,1,0,0]

m4 = [0,1,1,1,0,
      0,1,1,1,0,
      1,1,0,0,0,
      0,0,1,1,0,
      0,0,0,1,1]

m5 = [1,0,0,0,0,
      1,0,1,1,0,
      0,0,0,0,1,
      0,1,1,0,1,
      0,0,0,0,0]

trainSet = []
for a in [a1, a2, a3, a4, a5]:
    a.append('A')
    trainSet.append(a)

for b in [b1, b2, b3, b4, b5]:
    b.append('B')
    trainSet.append(b)

testSet = [m1, m2, m3, m4, m5]

knn = kNN(trainSet=trainSet, dim=25, k=5, distFunc=distance_manhattan)
for m in testSet:
    print(knn.classify(m))


