import math


def sigmoid(a):
    return 1.0 / (1 + math.exp(-a))


class LogisticRegression:

    def __init__(self, numFeatures):
        self.dim = numFeatures + 1
        self.theta = []
        for i in range(self.dim):
            self.theta.append(1)

    def hypothesis(self, x):
        s = 0
        for i in range(self.dim):
            s += x[i] * self.theta[i]
        return sigmoid(s)

    def gradient_descent(self, X, y, alpha=0.1, lam=0.1, iteration=1000):
        """
        Update theta by computing the gradient of the regularized cost function.
        :param X:
        :param y:
        :param alpha:
        :param lam:
        :param iteration:
        :return:
        """
        m = len(X) # number of training examples
        for count in range(iteration):
            self.theta[0] += (-alpha) * (1. / m) * sum([(self.hypothesis(X[i]) - y[i]) * X[i][0] for i in range(m)])
            for j in range(1, len(self.theta)):
                self.theta[j] += (- alpha) * ((1. / m) * sum([(self.hypothesis(X[i]) - y[i]) * X[i][j] for i in range(m)])
                                              + (lam / m) * self.theta[j])

    def predict(self, x):
        prob = self.hypothesis(x)
        return round(prob)


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
testSet = []
for data in [a1, a2, a3, a4, a5]:
    a = [1]
    for feature in data:
        a.append(feature)
    trainSet.append(a)
for data in [b1, b2, b3, b4, b5]:
    b = [1]
    for feature in data:
        b.append(feature)
    trainSet.append(b)
labels = [0, 0, 0, 0, 0, 1, 1, 1, 1, 1]

for data in [m1, m2, m3, m4, m5]:
    t = [1]
    for feature in data:
        t.append(feature)
    testSet.append(t)

lg = LogisticRegression(10)
lg.gradient_descent(trainSet, labels, lam=10)
print('Training Result:', [lg.predict(data) for data in trainSet])
print('Predictions for Mystery dataset:')
for data in testSet:
    if lg.predict(data) == 0:
        print(' A')
    else:
        print(' B')
print('theta:\n', lg.theta)

