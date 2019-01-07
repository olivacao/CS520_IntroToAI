import numpy as np
from skimage import io, data, color
from NeuralNetwork import NeuralNetwork
import matplotlib.pyplot as plt



def window(pic, i, j):
    """
    Get the surrounding 3x3 pixel window given pixel (i,j).
    :param pic: input picture, should have only one channel
    :param i:
    :param j:
    :return: a list of surrounding pixels
    """
    arr = []
    for p in range(i-1, i+2):
        for q in range(j-1, j+2):
            arr.append(pic[p, q])
    return arr


def color_normalize(pic):
    """
    Pre-process a true color picture, modify its RGB value to be 0, 64, 128, 192.
    :param pic:
    :return: picture after pre-process
    """
    for i in range(pic.shape[0]):
        for j in range(pic.shape[1]):
            pic[i, j, 0] = (pic[i, j, 0] // 64) * 64
            pic[i, j, 1] = (pic[i, j, 1] // 64) * 64
            pic[i, j, 2] = (pic[i, j, 2] // 64) * 64
    return pic


def to_onehot(num):
    """
    Calculate the one-hot code of decimal number 0-3.
    0: [1, 0, 0, 0]
    1: [0, 1, 0, 0]
    2: [0, 0, 1, 0]
    3: [0, 0, 0, 1]
    :param num: any decimal number between 0 to 3
    :return: its one-hot code
    """
    onehot = []
    for i in range(4):
        if i == num:
            onehot.append(1)
        else:
            onehot.append(0)
    return onehot


def onehot_recover(onehot):
    """
    Change one-hot code back to decimal format.
    :param onehot: one-hot code
    :return: corresponding decimal number
    """
    num = 0
    for i in range(4):
        if round(onehot[i]) == 1:
            num = i
    return num


def evaluate(original, prediction):
    """
    Calculate the average difference between the predicted img and the original img.
    Difference of two pixel is calculated by |(R1-R2) + (G1-G2) + (B1-B2)|/64
    :param original:
    :param prediction:
    :return:
    """
    result = (original - prediction) / 64
    count = np.sum(np.abs(result))
    return count / (result.shape[0] * result.shape[1])


# Set hyperparameters
layers = [9, 9, 4]
learning_rate = 0.1
epochs = 20000

# Pull image data from library or file
img_original = data.coffee()
# img_original = io.imread('./sun01.jpeg')
img = color_normalize(img_original)

# Separate train set and test set
train_data = color.rgb2gray(img[:, 0:int(img.shape[1]/2), :])
train_label = img[:, 0:int(img.shape[1]/2), :]
predict_data = color.rgb2gray(img[:, int(img.shape[1]/2):img.shape[1], :])
predict_label = img[:, int(img.shape[1]/2):img.shape[1], :]

# # Use two pics, one for train and another for test
# img_train = color_normalize(io.imread('sun01.jpeg'))
# img_test = color_normalize(io.imread('beach01.jpg'))
# train_data = color.rgb2gray(img_train)
# train_label = img_train
# predict_data = color.rgb2gray(img_test)
# predict_label = img_test
# img_original = img_train


# Set up nn for R value
nn_r = NeuralNetwork(layers, 'tanh')
X = []
y_r = []

# Set up nn for G value
nn_g = NeuralNetwork(layers, 'tanh')
y_g = []

# Set up nn for B value
nn_b = NeuralNetwork(layers, 'tanh')
y_b = []

# Set train data and label
for i in range(1, train_data.shape[0]-1):
    for j in range(1, train_data.shape[1]-1):
        X.append(window(train_data, i, j))
        y_r.append(to_onehot(train_label[i, j, 0] / 64))
        y_g.append(to_onehot(train_label[i, j, 1] / 64))
        y_b.append(to_onehot(train_label[i, j, 2] / 64))

# Train
nn_r.fit(X, y_r, learning_rate=learning_rate, epochs=epochs)
nn_g.fit(X, y_g, learning_rate=learning_rate, epochs=epochs)
nn_b.fit(X, y_b, learning_rate=learning_rate, epochs=epochs)

print("R", nn_r.predict(window(predict_data, 100, 100)) * 64)
print("G", nn_g.predict(window(predict_data, 100, 100)) * 64)
print("B", nn_b.predict(window(predict_data, 100, 100)) * 64)

# Make prediction
result = np.zeros([predict_data.shape[0], predict_data.shape[1], 3], dtype=np.uint8)

for i in range(1, predict_data.shape[0]-1):
    for j in range(1, predict_data.shape[1]-1):
        result[i, j, 0] = onehot_recover(nn_r.predict(window(predict_data, i, j))) * 64
        result[i, j, 1] = onehot_recover(nn_g.predict(window(predict_data, i, j))) * 64
        result[i, j, 2] = onehot_recover(nn_b.predict(window(predict_data, i, j))) * 64

result.astype(np.int)

# Show result
plt.figure(figsize=(12, 12))

plt.subplot(2,2,1)
plt.title('Original Pic')
plt.imshow(img_original)
# plt.axis('off')

plt.subplot(2,2,3)
plt.title('Normalized Pic')
plt.imshow(predict_label)
# plt.axis('off')

plt.subplot(2,2,4)
plt.title('Predicted Pic')
plt.imshow(result)
# plt.axis('off')

plt.show()
io.imsave('result.jpg', result)

# Print evaluation
print("Difference:", evaluate(predict_label, result))
