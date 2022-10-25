# Import libraries
import matplotlib.pyplot as plt
import csv
data=[]
import matplotlib.pyplot as plt
import numpy as np

# Random test data
np.random.seed(19680801)
all_data = [np.random.normal(0, std, size=100) for std in range(1, 4)]
labels = ['x1', 'x2', 'x3']
sample_nums=["10","20","50","100","200","500"]
title1="Orion Simulation"
title2="Orion Importance Sampling"
y_label="Result of the analysis"
x_label="Number of simulations"
data=[]
with open('./orion_sim_conv_meas.csv', encoding='UTF8') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    for row in csv_reader:
        print(row)
        data.append([float(d) for d in row])

data2=[]
with open('./orion_is_conv_meas.csv', encoding='UTF8') as csv_file:
    csv_reader = csv.reader(csv_file, delimiter=',')
    for row in csv_reader:
        print(row)
        data2.append([float(d) for d in row])

fig, (ax1, ax2) = plt.subplots(nrows=1, ncols=2, figsize=(12, 4))

# rectangular box plot
bplot1 = ax1.boxplot(data,
                     vert=True,  # vertical box alignment
                     #patch_artist=True ,  # fill with color
                     labels=sample_nums)  # will be used to label x-ticks
ax1.set_title(title1)

# notch shape box plot
bplot2 = ax2.boxplot(data2,
                     #notch=True,  # notch shape
                     vert=True,  # vertical box alignment
                     #patch_artist=True,  # fill with color
                     labels=sample_nums)  # will be used to label x-ticks
ax2.set_title(title2)

# fill with colors
#colors = ['pink', 'lightblue', 'lightgreen']
#for bplot in (bplot1, bplot2):
#    for patch, color in zip(bplot['boxes'], colors):
#        patch.set_facecolor(color)

# adding horizontal grid lines
for ax in [ax1, ax2]:
    ax.yaxis.grid(True)
    ax.set_xlabel(x_label)
    ax.set_ylabel(y_label)

plt.show()


