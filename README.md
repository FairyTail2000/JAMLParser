# JAMLParser
This is a parser for JAML

This parses JAML (Just Another Markup Language), it is invented by me and has the following specifications:

The start of data is an opening bracket ({) followed by a newline.
In the next line the data begins with the name of the elemt.
The name gets terminated with a double point (:).
The data now starts with an quotation mark (")  and ends with an quotation marks (").
The use of quotation marks (") inside of data is not implemented.
Multiline data is supported.
The data gets finally terminated with a closing bracket (}).
When new data follows, followed by a newline.

Why did i made this?
I just wanted a simple and easy to parseable dataformat with no complex features or anything, I could have just used json but no.
