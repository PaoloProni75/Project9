# Project9
Design Challenge 9

## Challenge
The challenge is to create a component that can access services from different bases of the Open Data Hub API, mapping a parameter from different fields of the APIs. As an example, a date parameter can be mapped to query each API in a different field. The output from both APIs can have different formats, but should be possible to order the results based on different fields. Using the same date example, the results can be ordered by dates that are defined in different fields on both APIs. As an example, that component could be used when retrieving points of interest or events from the mobility and tourism APIs.

## Requirements
A component that invoke two classes that call Open Data Hub APIs from different databases (these classes should have the same interface and should be plugged in).
- A parameter passed to the component can be used to query each API based on a different field (in other words, the field to be queried, in each API, can have a different name).
- A parameter passed to the component can be used to order each API based on a different field (in other words, the field to be used in ordering, in each API, can have a different name).
- The result from both APIs should be returned in a single result.

## Test
Choose two Open Data Hub APIs, one from mobility and one tourism APIs, to test your solution.
- Create tests that return a query, using both APIs, that have parameters that map to different fields.
- Create tests that return a query, using both APIs, that is ordered by an information that is present in different fields.
