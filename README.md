# pathFinder

A pathfinding algorithm visulization application built with JavaFX and JavaSwing in Eclipse.

## Pre-req

Any version of java jdk

## How to run the program

### From command line

go to the project folder, and run `java -jar run.jar`


### In eclipse

Import the project to eclipse, then run with UI

## How to use the simulator 

- When the program is opened, it should look like this: 

<img width="697" alt="pathfinder_1" src="https://user-images.githubusercontent.com/57467554/191152670-29cd30ba-df8d-4e07-b12e-f4a4c3584c71.png">

By adjusting the size bar, the user can modify the number of simulating squares (steps) in the graph. (Min 20, Max 40)

The user can also select algorithm from the algorithm dropdown list.

- Next, set the simulation starting coordinate by clicking on the graph

<img width="695" alt="pf_start" src="https://user-images.githubusercontent.com/57467554/191152963-5c61d64c-7811-44be-a077-e10dd928927b.png">


- Similarly, set the simulation destination coordinate by clicking again

<img width="696" alt="pf_end" src="https://user-images.githubusercontent.com/57467554/191153170-e820e35e-63c7-4579-bf2f-a45e4d661ddb.png">

- Anytime during selecting tiles, you can click clear to remove all of current selection by clicking the `Clear` button

-  After start and end have been set, you can randomly generate obstacles along the way by clicking the `random generate` button

<img width="694" alt="pf_random" src="https://user-images.githubusercontent.com/57467554/191153253-94448e73-d623-428d-aa29-049eb4f6e9a5.png">

- Or you can click and drag on the graph to create your own obstacle walls. 
<img width="695" alt="pf_draw" src="https://user-images.githubusercontent.com/57467554/191153400-7f84a4ea-575b-4edf-8ca2-8b832452b358.png">

- After everything is ready, click `Start` to start the visualization. 

The Blue line is the path from start to end with the selected algorithm. 

<img width="695" alt="pf_finished" src="https://user-images.githubusercontent.com/57467554/191153816-7a5ae7e5-eb9e-433d-b793-2aba4bd646dd.png">
