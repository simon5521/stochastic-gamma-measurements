from cond_simulator import simulate, stochmodel

import math
from math import exp
import os
import traceback

import torch
import torch.distributions.constraints as constraints
import numpy as np
import pyro
from pyro.infer import SVI, Trace_ELBO
from pyro.optim import Adam
import pyro.distributions as dist

import matplotlib.pyplot as plt
from statistics import mean, median, variance

data=[]
num_samples=200
VISUAL=True


def model():
    global stochmodel
    stochmodel.voterFailureRate=pyro.sample("param_0",pyro.distributions.Uniform(low=torch.tensor(0.0),high=torch.tensor(10.0))).clone().detach()
    result=simulate()
    data.append(result)
    pyro.sample("cond",dist.Normal(torch.tensor(0.4),torch.tensor(0.05)),obs=torch.tensor(result))

def guide():
    mean=pyro.param("mean",torch.tensor(2.0), constraint=constraints.positive)
    scale=pyro.param("scale",torch.tensor(0.5), constraint=constraints.positive)
    stochmodel.voterFailureRate=pyro.sample("param_0",dist.Normal(torch.tensor(mean.clone().detach()),torch.tensor(scale.clone().detach()))).clone().detach()
    simulate()
    

initial_lr = 0.1
gamma = 1.0  # final learning rate will be gamma * initial_lr
lrd = gamma ** (1 / num_samples)
optimizer = pyro.optim.ClippedAdam({'lr': initial_lr, 'lrd': lrd})

svi = SVI(model, guide, optimizer, loss=Trace_ELBO())
kl_diff = list()
# do gradient steps
for step in range(num_samples):
    d=svi.step()
    kl_diff.append(d)
    if VISUAL and ( step % 5 == 0):
        print("Simulation step: ", step, " KL diff = ",d)
#results.append(dist.Normal(pyro.param("mean"),pyro.param("scale")).mean.item())
if VISUAL:    
    print("infered mean = ",pyro.param("mean").item())
    print("infered concentration = ",pyro.param("scale").item())

    fig1, a1 = plt.subplots()
    fig2, a2 = plt.subplots()
    a1.plot(kl_diff)
    a1.set_ylabel('Estimated Kullback-Leibler divergence')
    a1.set_xlabel('step')
    a1.set_title("model fitting process")
    a2.hist(data,bins=20, density=1)
    a2.set_ylabel('density')
    a2.set_xlabel('delay (s)')
    a2.set_title("model fitting results")
    plt.show()