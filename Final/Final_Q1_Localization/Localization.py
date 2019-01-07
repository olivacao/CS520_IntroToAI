import numpy as np
from random import random
from BFS import *


def read_data(filename='Maze.txt'):
    file = open(filename, "r")
    data = file.readlines()
    arr = []
    for line in data:
        arr_line = []
        for num in list(line):
            if num != '\n':
                if num == 'G':
                    arr_line.append(-1)
                else:
                    arr_line.append(int(num))
        arr.append(arr_line)
    file.close()
    return np.array(arr)


def get_maze_info(maze):
    white = 0
    black = 0
    for i in range(maze.shape[0]):
        for j in range(maze.shape[1]):
            if maze[i, j] == 0:
                white += 1
            if maze[i, j] == 1:
                black += 1
            if maze[i, j] == -1:
                white += 1
                G = [i, j]
    return [white, black, G]


def build_sequence():
    sequence = ''
    while True:
        direction = input('Please input Direction(L/R/U/D), "Q" to quit: ')
        if direction == 'Q':
            break
        num = input('Num of Steps: ')
        for i in range(int(num)):
            sequence += direction
        print(sequence)
    return sequence


class MazeSolvingBot:

    def __init__(self, maze):
        self.maze = maze
        info = get_maze_info(maze)
        self.white = info[0]
        self.black = info[1]
        self.goal = info[2]
        self.prob_matrix = np.zeros(self.maze.shape)
        for i in range(self.maze.shape[0]):
            for j in range(self.maze.shape[1]):
                if maze[i, j] != 1:
                    self.prob_matrix[i, j] = 1 / self.white

    def get_goal_prob(self):
        return self.prob_matrix[self.goal[0], self.goal[1]]

    def right(self):
        """
        Move to the right.
        :return: The probability matrix after performing the move.
        """
        new_prob_matrix = np.zeros(self.maze.shape)
        for i in range(1, self.maze.shape[0] - 1):
            for j in range(1, self.maze.shape[1] - 1):
                # skip if it's wall
                if self.maze[i, j] == 1:
                    continue
                # if the right side of the cell is not wall, transfer its prob to its right
                if self.maze[i, j+1] != 1:
                    new_prob_matrix[i, j+1] += self.prob_matrix[i, j]
                # if the right side is wall, keep its prob in place
                else:
                    new_prob_matrix[i, j] += self.prob_matrix[i, j]
        return new_prob_matrix

    def left(self):
        """
        Move to the left.
        :return: The probability matrix after performing the move.
        """
        new_prob_matrix = np.zeros(self.maze.shape)
        for i in range(1, self.maze.shape[0] - 1):
            for j in range(1, self.maze.shape[1] - 1):
                # skip if it's wall
                if self.maze[i, j] == 1:
                    continue
                # if the right side of the cell is not wall, transfer its prob to its right
                if self.maze[i, j-1] != 1:
                    new_prob_matrix[i, j-1] += self.prob_matrix[i, j]
                # if the right side is wall, keep its prob in place
                else:
                    new_prob_matrix[i, j] += self.prob_matrix[i, j]
        return new_prob_matrix

    def up(self):
        """
        Move up.
        :return: The probability matrix after performing the move.
        """
        new_prob_matrix = np.zeros(self.maze.shape)
        for i in range(1, self.maze.shape[0] - 1):
            for j in range(1, self.maze.shape[1] - 1):
                # skip if it's wall
                if self.maze[i, j] == 1:
                    continue
                # if the right side of the cell is not wall, transfer its prob to its right
                if self.maze[i-1, j] != 1:
                    new_prob_matrix[i-1, j] += self.prob_matrix[i, j]
                # if the right side is wall, keep its prob in place
                else:
                    new_prob_matrix[i, j] += self.prob_matrix[i, j]
        return new_prob_matrix

    def down(self):
        """
        Move down.
        :return: The probability matrix after performing the move.
        """
        new_prob_matrix = np.zeros(self.maze.shape)
        for i in range(1, self.maze.shape[0] - 1):
            for j in range(1, self.maze.shape[1] - 1):
                # skip if it's wall
                if self.maze[i, j] == 1:
                    continue
                # if the right side of the cell is not wall, transfer its prob to its right
                if self.maze[i+1, j] != 1:
                    new_prob_matrix[i+1, j] += self.prob_matrix[i, j]
                # if the right side is wall, keep its prob in place
                else:
                    new_prob_matrix[i, j] += self.prob_matrix[i, j]
        return new_prob_matrix

    def perform_moves(self, sequence):
        """
        Perform a sequence of moves.
        :param sequence: a sequence of directions
        :return:
        """
        for instruction in sequence:
            if instruction == 'R':
                self.prob_matrix = self.right()
            if instruction == 'L':
                self.prob_matrix = self.left()
            if instruction == 'U':
                self.prob_matrix = self.up()
            if instruction == 'D':
                self.prob_matrix = self.down()

    def evaluate(self, prob_matrix):
        """
        Evaluate a move by counting the number of possible positions.
        :param prob_matrix: the probability matrix returned by a move
        :return:
        """
        num = 0
        for i in range(prob_matrix.shape[0]):
            for j in range(prob_matrix.shape[1]):
                if prob_matrix[i, j] != 0:
                    num += 1
        return num

    def print_board(self):
        for i in range(self.prob_matrix.shape[0]):
            for j in range(self.prob_matrix.shape[1]):
                if self.maze[i, j] == 1:
                    print('*', end=' ')
                elif self.prob_matrix[i, j] == 0:
                    print(' ', end=' ')
                else:
                    prob = int(round(self.prob_matrix[i, j] * 10))
                    print(prob, end=' ')
            print()

    def search_for_sequence_at_least_half(self, num):
        """
        Try to find a sequence that makes the probability of the goal point is at least greater than 0.5.
        :param num: number of iterations
        :return: a sequence of directions
        """
        sequence = ''

        for i in range(num):

            if self.evaluate(self.prob_matrix) < 30:
                cells = []
                for i in range(self.prob_matrix.shape[0]):
                    for j in range(self.prob_matrix.shape[1]):
                        if self.prob_matrix[i, j] != 0:
                            cells.append([i, j])
                cells.sort(key=lambda x: self.prob_matrix[x[0], x[1]])
                if self.prob_matrix[cells[-1][0], cells[-1][1]] > 0.5:
                    # if the largest prob is greater than 0.5, move it to goal and finish
                    bfs = BFS(self.maze)
                    subseq = directions(bfs.search(cells[-1], self.goal))
                    self.perform_moves(subseq)
                    sequence += subseq
                    break
                else:
                    # direct the largest to the second largest
                    bfs = BFS(self.maze)
                    subseq = directions(bfs.search(cells[-2], cells[-1]))
                    self.perform_moves(subseq)
                    sequence += subseq
                    continue

            if self.evaluate(self.prob_matrix) < 80:
                prob = 0.8
            else:
                prob = 0.01

            # choose the direction randomly
            if random() < prob:

                rand = random()
                last_move = sequence[-1]
                if last_move == 'R':
                    if rand < 0.46:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    elif rand < 0.64:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.82:
                        self.prob_matrix = self.left()
                        sequence += 'L'
                    else:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                if last_move == 'L':
                    if rand < 0.46:
                        self.prob_matrix = self.left()
                        sequence += 'L'
                    elif rand < 0.64:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.82:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    else:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                if last_move == 'U':
                    if rand < 0.46:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                    elif rand < 0.64:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.82:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    else:
                        self.prob_matrix = self.left()
                        sequence += 'L'
                if last_move == 'D':
                    if rand < 0.46:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.64:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                    elif rand < 0.82:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    else:
                        self.prob_matrix = self.left()
                        sequence += 'L'

        # choose the direction with most overlapping
            else:

                R = self.evaluate(self.right())
                L = self.evaluate(self.left())
                U = self.evaluate(self.up())
                D = self.evaluate(self.down())
                m = min([R, L, U, D])
                if m == R:
                    self.prob_matrix = self.right()
                    sequence += 'R'
                if m == L:
                    self.prob_matrix = self.left()
                    sequence += 'L'
                if m == U:
                    self.prob_matrix = self.up()
                    sequence += 'U'
                if m == D:
                    self.prob_matrix = self.down()
                    sequence += 'D'
        return sequence

    def search_for_sequence_merge_all(self, num):
        """
        Try to find a sequence that moves bot from all position to the goal point.
        :param num: number of iterations
        :return: a sequence of directions.
        """
        sequence = ''

        for i in range(num):

            if self.evaluate(self.prob_matrix) < 30 and self.evaluate(self.prob_matrix) != 1:
                cells = []
                for i in range(self.prob_matrix.shape[0]):
                    for j in range(self.prob_matrix.shape[1]):
                        if self.prob_matrix[i, j] != 0:
                            cells.append([i, j])
                cells.sort(key=lambda x: self.prob_matrix[x[0], x[1]])

                # direct the largest to the second largest
                bfs = BFS(self.maze)
                subseq = directions(bfs.search(cells[-2], cells[-1]))
                self.perform_moves(subseq)
                sequence += subseq
                continue

                # if there is only one possible position, move it to the goal point and finish
            elif self.evaluate(self.prob_matrix) == 1:
                for i in range(self.prob_matrix.shape[0]):
                    for j in range(self.prob_matrix.shape[1]):
                        if self.prob_matrix[i, j] != 0:
                            cell = [i, j]
                bfs = BFS(self.maze)
                subseq = directions(bfs.search(cell, self.goal))
                self.perform_moves(subseq)
                sequence += subseq
                break

            if self.evaluate(self.prob_matrix) < 80:
                prob = 0.8
            else:
                prob = 0.01

            # choose the direction randomly
            if random() < prob:

                rand = random()
                last_move = sequence[-1]
                if last_move == 'R':
                    if rand < 0.46:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    elif rand < 0.64:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.82:
                        self.prob_matrix = self.left()
                        sequence += 'L'
                    else:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                if last_move == 'L':
                    if rand < 0.46:
                        self.prob_matrix = self.left()
                        sequence += 'L'
                    elif rand < 0.64:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.82:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    else:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                if last_move == 'U':
                    if rand < 0.46:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                    elif rand < 0.64:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.82:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    else:
                        self.prob_matrix = self.left()
                        sequence += 'L'
                if last_move == 'D':
                    if rand < 0.46:
                        self.prob_matrix = self.down()
                        sequence += 'D'
                    elif rand < 0.64:
                        self.prob_matrix = self.up()
                        sequence += 'U'
                    elif rand < 0.82:
                        self.prob_matrix = self.right()
                        sequence += 'R'
                    else:
                        self.prob_matrix = self.left()
                        sequence += 'L'

        # choose the direction with most overlapping
            else:

                R = self.evaluate(self.right())
                L = self.evaluate(self.left())
                U = self.evaluate(self.up())
                D = self.evaluate(self.down())
                m = min([R, L, U, D])
                if m == R:
                    self.prob_matrix = self.right()
                    sequence += 'R'
                if m == L:
                    self.prob_matrix = self.left()
                    sequence += 'L'
                if m == U:
                    self.prob_matrix = self.up()
                    sequence += 'U'
                if m == D:
                    self.prob_matrix = self.down()
                    sequence += 'D'
        return sequence

    def observe(self, cell):
        """
        For a given cell, return the number of surrounding blocked cells.
        :param cell: in format of [i, j]
        :return:
        """
        count = 0
        for i in range(max(0, cell[0] - 1), min(self.maze.shape[0], cell[0] + 2)):
            for j in range(max(0, cell[1] - 1), min(self.maze.shape[1], cell[1] + 2)):
                if self.maze[i, j] == 1:
                    count += 1
        return count

    def get_observation(self):
        """
        Return a matrix to keep the observation of all cells in maze
        :return:
        """
        observation = np.zeros(self.maze.shape)
        for i in range(self.maze.shape[0]):
            for j in range(self.maze.shape[1]):
                if self.maze[i, j] != 1:
                    observation[i, j] = self.observe([i, j])
        return observation

    def most_likely_cells_helper(self, y):
        """
        Given a observation y, return a list of possible cells.
        :param y: observation
        :return: a list of possible cells
        """
        observation = self.get_observation()
        cells = []
        for i in range(self.maze.shape[0]):
            for j in range(self.maze.shape[1]):
                if observation[i, j] == y:
                    cells.append([i, j])
        return cells

    def most_likely_cells(self, Y, A):
        """
        Take a sequence of observations {Y0, Y1, ..., Yn} and a sequence of actions {A0, A1, ..., An-1}
        and return the cells you are most likely to be in.
        :param Y: a list of observations
        :param A: a string of actions
        :return: highest probability, a list of most likely cells
        """
        new_cells = self.most_likely_cells_helper(Y[0])
        for i in range(len(A)):
            cells = new_cells
            new_cells = []
            action = A[i]
            for cell in cells:
                if action == 'L':
                    cell_after_action = [cell[0], cell[1] - 1]
                elif action == 'R':
                    cell_after_action = [cell[0], cell[1] + 1]
                elif action == 'U':
                    cell_after_action = [cell[0] - 1, cell[1]]
                elif action == 'D':
                    cell_after_action = [cell[0] + 1, cell[1]]

                if self.maze[cell_after_action[0], cell_after_action[1]] == 1:
                    cell_after_action = cell

                if self.observe(cell_after_action) == Y[i+1]:
                    new_cells.append(cell_after_action)

        # calculate the probability for each cell
        new_prob_matrix = np.zeros(bot.maze.shape)
        for cell in new_cells:
                new_prob_matrix[cell[0], cell[1]] += 1. / len(new_cells)

        # Find highest probability
        max_prob = 0
        for i in range(new_prob_matrix.shape[0]):
            for j in range(new_prob_matrix.shape[1]):
                if new_prob_matrix[i, j] > max_prob:
                    max_prob = new_prob_matrix[i, j]

        # Find all most likely cells
        most_likely_cells = []
        for i in range(new_prob_matrix.shape[0]):
            for j in range(new_prob_matrix.shape[1]):
                if new_prob_matrix[i, j] == max_prob:
                    most_likely_cells.append([i, j])

        return max_prob, most_likely_cells


# To test, comment out one question at a time

# # ----------------Question a---------------------
# maze = read_data()
# bot = MazeSolvingBot(maze)
# print('Number of reachable cells:', bot.white)
# print('Probability:', 1./bot.white)

# ----------------Question b---------------------
maze = read_data()
bot = MazeSolvingBot(maze)
sequence = bot.search_for_sequence_at_least_half(5000)
print(sequence)
print('Length:', len(sequence))
bot.print_board()
print('evaluate: ', bot.evaluate(bot.prob_matrix))
print('Goal Prob:', bot.get_goal_prob())

# # ----------------Question c---------------------
# maze = read_data()
# bot = MazeSolvingBot(maze)
# sequence = bot.search_for_sequence_merge_all(5000)
# print(sequence)
# print('Length:', len(sequence))
# bot.print_board()
# print('evaluate: ', bot.evaluate(bot.prob_matrix))
# print('Goal Prob:', bot.get_goal_prob())

# # ----------------Question d1---------------------
# bot = MazeSolvingBot(read_data())
# new_cells = bot.most_likely_cells_helper(5)
#
# # Perform moves
# for i in range(2):
#     cells = new_cells
#     new_cells = []
#     for cell in cells:
#         left = [cell[0], cell[1] - 1]
#         if bot.maze[left[0], left[1]] == 1:
#             left = cell
#
#         if bot.observe(left) == 5:
#             new_cells.append(left)
#
# # The final probability of each cell
# new_prob_matrix = np.zeros(bot.maze.shape)
# for cell in new_cells:
#     new_prob_matrix[cell[0], cell[1]] += 1. / len(new_cells)
# print(new_prob_matrix)
#
# # Find highest probability
# max_prob = 0
# for i in range(new_prob_matrix.shape[0]):
#     for j in range(new_prob_matrix.shape[1]):
#         if new_prob_matrix[i, j] > max_prob:
#             max_prob = new_prob_matrix[i, j]
# print('Highest Prob:', max_prob)
#
# # Use a dictionary to store the cells with same probability
# final_prob = {}
# for i in range(new_prob_matrix.shape[0]):
#     for j in range(new_prob_matrix.shape[1]):
#         if not final_prob.get(new_prob_matrix[i, j]):
#             final_prob[new_prob_matrix[i, j]] = [[i, j]]
#         else:
#             final_prob[new_prob_matrix[i, j]].append([i, j])
# print(final_prob)
# print(final_prob.keys())
# print(final_prob.get(0.01056338028169014))
# print(final_prob.get(0.0035211267605633804))


# # ----------------Question d2---------------------
# bot = MazeSolvingBot(read_data())
# prob, cells = bot.most_likely_cells([5, 5, 5], 'LL')
# print('Probability: ', prob)
# print(cells)
# print('Number of most likely cells:', len(cells))

# # -------------------Bonus-----------------------
# bot = MazeSolvingBot(read_data())
# bot.goal = [10, 12]
# sequence = bot.search_for_sequence_merge_all(5000)
# print(sequence)
# print('Length:', len(sequence))
# bot.print_board()
# print('evaluate: ', bot.evaluate(bot.prob_matrix))
# print('Goal Prob:', bot.get_goal_prob())

# # ------------To verify a sequence---------------
# s = 'RRRDRRRRDRDDDDLDDDRRRURRRRRDRRRRDDLDDRRRRUUURRDDDDDDDDDDLLDDRRDDDDDDDDDDLLDDRRDDRRDDDLLLLLLLLLLLLLLDDDDRRRRRRRRRRRRRRUUURRDDDDDDDDRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRLLLLLLLLLLLLLLLLLLUUUUUUUURRRRRRRRRRRRRRUUUUULLDDDLLLULLUUU'
# bot = MazeSolvingBot(read_data())
# bot.perform_moves(s)
# bot.print_board()
# print(s)
# print('Length:', len(s))
# print(bot.get_goal_prob())
# print(bot.evaluate(bot.prob_matrix))


