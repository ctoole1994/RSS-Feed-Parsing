# Technical Project Assumptions:

I am assuming the format of the RSS XML will conform to the RSS 2.0 specification.

For date comparison, I will be assuming a format of RFC 1123. The RFC 822 format is in the specification for RSS,
with the exception that the year may be 2 or 4 characters.

For RSS date comparison, I will be assuming a comparison to the UTC timezone, and convert local and RSS times to compare.

For given days, I will assume that a positive integer is passed.