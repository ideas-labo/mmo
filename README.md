# MMO: Meta Multi-Objectivization for Software Configuration Tuning

This repository contains the data and code for the following paper:

> Tao Chen and Miqing Li. 2021. MMO: Meta Multi-Objectivization for Software Configuration Tuning.

## Introduction

Automatically tuning software configuration for optimizing a single performance attribute (e.g., minimizing latency) is not trivial, due to the nature of the configuration systems (e.g., complex landscape and expensive measurement). To deal with the problem, existing work has been focusing on developing various effective optimizers. However, a prominent issue that all these optimizers need to take care of is how to avoid the search being trapped in local optima — a hard nut to crack for software configuration tuning due to its rugged and sparse landscape, and neighboring configurations tending to behave very differently. Overcoming such in an expensive mea- surement setting is even more challenging. In this work, we take a different perspective to tackle this issue. Instead of focusing on improving the optimizer, we work on the level of optimization model. We do this by proposing a meta multi-objectivization model (MMO) that considers an auxiliary performance objective (e.g., throughput in addition to latency). What makes this model unique is that we do not optimize the auxiliary performance objective, but rather use it to make similarly-performing while different configurations less comparable (i.e. Pareto nondominated to each other), thus preventing the search from being trapped in local optima.


## Repository Content

The `data` folder contains all the raw data as reported in the paper; most of the structures are self-explained but we wish to highlight the following:

* The data under the folder `1.0-0.0` and `0.0-1.0` are for the single-objective optimizers. The former uses O1 as the target performance objective while the latter uses O2 as the target. The data under other folders named by the subject systems are for the MMO and PMO. The result under the weight folder `1.0` are for MMO while all other folders represent different weight values, containing the data for MMO-FSE.

* For those data of MMO, MMO-FSE, and PMO, the folder `0` and `1` denote using uses O1 and O2 as the target performance objective, respectively.

* In the lowest-level folder where the data is stored (i.e., the `sas` folder), `SolutionSet.rtf` contains the results over all repeated runs; `SolutionSetWithMeasurement.rtf` records the results over different numbers of measurements.

The [`code`](https://github.com/ideas-labo/mmo/tree/main/code) folder contains all the information about the source code, as well as an executable jar file in the [`executable`](https://github.com/ideas-labo/mmo/tree/main/executable) folder .


## Running the Experiments

To run the experiments, one can download the `mmo-experiments.jar` from the aforementioned repository (under the [`executable`](https://github.com/ideas-labo/mmo/tree/main/executable) folder). Since the artifacts were written in Java, we assume that the JDK/JRE has already been installed. Next, one can run the code using `java -jar mmo-experiments.jar [subject] [runs]`, where `[subject]` and `[runs]` denote the subject software system and the number of repeated run (this is an integer and 50 is the default if it is not specified), respectively. The keyword for the systems/environments used in the paper are: 

1. trimesh 
1. x264
1. storm-wc
1. storm-rs
1. storm-sol
1. dnn-dsr
1. dnn-coffee
1. LSTM 

For example, running `java -jar mmo-experiments.jar trimesh` would execute experiments on the `trimesh` software for 30 repeated runs.

For each software system, the experiment consists of the runs for MMO instances with all seven weight values, PMO and the four state-of-the-art single-objective optimizers. All the outputs would be stored in the `results` folder at the same directory as the executable jar file.

All the measurement data of the subject configurable systems have been placed inside the `mmo-experiments.jar`.


