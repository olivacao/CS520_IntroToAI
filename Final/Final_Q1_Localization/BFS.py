import numpy as np


class BFS:

    def __init__(self, maze):
        self.maze = maze
        self.marked = np.zeros(maze.shape)

    def adjacent(self, cell):
        """
        Return a list of adjacent untouched cells
        :param cell: in format of [i, j]
        :return:
        """
        cells = []
        if cell[0] + 1 < self.maze.shape[0]:
            if self.maze[cell[0] + 1, cell[1]] != 1 and self.marked[cell[0] + 1, cell[1]] != 1:
                cells.append([cell[0] + 1, cell[1]])
        if cell[0] - 1 > 0:
            if self.maze[cell[0] - 1, cell[1]] != 1 and self.marked[cell[0] - 1, cell[1]] != 1:
                cells.append([cell[0] - 1, cell[1]])
        if cell[1] + 1 < self.maze.shape[1]:
            if self.maze[cell[0], cell[1] + 1] != 1 and self.marked[cell[0], cell[1] + 1] != 1:
                cells.append([cell[0], cell[1] + 1])
        if cell[1] - 1 > 0:
            if self.maze[cell[0], cell[1] - 1] != 1 and self.marked[cell[0], cell[1] - 1] != 1:
                cells.append([cell[0], cell[1] - 1])
        return cells

    def search(self, start, end):
        """
        Search for the shortest path from start to end
        :param start: in format of [i, j]
        :param end: in format of [i, j]
        :return:
        """
        queue = []
        self.marked[start[0], start[1]] = 1
        queue.append([start])
        while queue:
            path = queue.pop(0)
            cell = path[-1]
            if cell == end:
                return path
            for neighbor in self.adjacent(cell):
                new_path = list(path)
                self.marked[neighbor[0], neighbor[1]] = 1
                new_path.append(neighbor)
                queue.append(new_path)


def directions(path):
    """
    Given a list of cells return the sequence of directions.
    :param path:
    :return:
    """
    curr = path[0]
    direct = ''
    for cell in path[1:]:
        if [curr[0] + 1, curr[1]] == cell:
            direct += 'D'
        elif [curr[0] - 1, curr[1]] == cell:
            direct += 'U'
        elif [curr[0], curr[1] + 1] == cell:
            direct += 'R'
        elif [curr[0], curr[1] - 1] == cell:
            direct += 'L'
        curr = cell
    return direct




