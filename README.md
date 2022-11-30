<div style="color: darkgreen;">

World-Changing Event + 2 Entity Changes

I copied my current (fully working w/ AStarPathing) code into this repository, so this will be our collective code-base for the final project.

Start by taking a look at my draw.io file for a general idea of my project structure.

Here is our current idea - which meets all the project requirements, but will likely change a little as we work on it

World Changing Event: Lighting strike upon mouse click, effects 3x3 area. Grass -> burned grass. Trees, stump, and sapling -> disappear (for now), dude is changed to skeleton, fairies/house/water/other world tiles are ignored.

Changed Entity: Dude -> Skelton, if within event range. Has random movement, this will need to be coded from scratch (+ possibly change existing dudes into skeletons)

New Entity: Void, paths towards the nearest fairy and deletes it if within range. Most of this code is similar to existing entities

What I'm working on: 
-Creating new entity classes for skeleton and void

What you can work on: 
-Find an appropriate animation for the skeleton, (Void as well if you have time)

I'm gonna try and frontload the amount of work that I'm doing so I don't have to grind this out next week, but don't feel the need to do the same

IMPORTANT don't commit any code to main in the repository if it isn't fully functional, use different branches for code that is still "in progress"
