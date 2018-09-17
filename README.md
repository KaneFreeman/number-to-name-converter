# number-to-name-converter
Takes a number as a string, and converts it to the english language equivalent.

I was using this project to test out parallel streams and their performance on a task like this versus sequential streams. Even with the largest number I planned to support ([per wikipedia](https://en.wikipedia.org/wiki/Names_of_large_numbers#Standard_dictionary_numbers)) the sequential tests completed faster than the parallel (tested with `187,531,531,520,036,100,187,531,520,036,776,890,188,100,187,531,520,036,163,270,013,110`). Granted the difference was on the nanosecond level, less than 1ms, so not a big difference.

## Example
`14,345,001` -> `fourteen million, three hundred and fourty five thousand, one`
