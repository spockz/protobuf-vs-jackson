
Benchmark - Protcol Buffers 3 vs Jackson JSON
------

This is a simple comparison of performance and size between 
Jackson (w/ Afterburner) and Protocol Buffers version 3.


I came across [an article][article] comparing the two, showing PB with worse performance, in
some tests nearly 2-times slower. 
 
My initial reaction was: this can't be correct.  When browsing the [benchmark's
source code][source] I noticed the PB version was adversely impacted by converting
JodaTime to / from string, while Jackson was using a more efficient
serialization.  The profiler showed JodaTime conversion was taking up most of
the time, making PB look far worse.

Usage
-----

    ./gradlew run


Example Run
-----

    JDK 8
    Macbook Pro (early-2011)
    2.2 GHz Intel Core i7
    8 GB 1600 MHz DDR3
    
    jacksonVersion = '2.5.1'
    protobufVersion = '3.0.2'

    loops = 10
    iters = 20,000

    times in milliseconds
    sizes in bytes (protobuf) and characters (jackson, afterburner, protobuf-json)

                             test      min      max      avg
------------------------------------------------------------

    with 0 children
             jackson serialization      9.4    114.1     27.6
           jackson deserialization     16.6    133.6     34.6
         afterburner serialization      9.7     55.0     18.9
       afterburner deserialization     15.7     23.8     18.7
            protobuf serialization      1.2     15.9      3.8
          protobuf deserialization      2.7     23.4      6.2
       protobuf-json serialization     38.4    228.7     70.7
     protobuf-json deserialization     47.8    224.8     78.2
    
    encoded sizes:
                           jackson   104
                       afterburner   104
                          protobuf   26
                     protobuf-json   95
    
    
    with 8 children
             jackson serialization     64.5     87.3     71.2
           jackson deserialization    133.9    171.2    144.5
         afterburner serialization     63.0     77.7     68.4
       afterburner deserialization    134.1    157.4    145.6
            protobuf serialization     10.0     91.6     19.6
          protobuf deserialization     25.3     70.0     31.5
       protobuf-json serialization    365.2    463.7    389.5
     protobuf-json deserialization    334.1    442.7    360.3
    
    encoded sizes:
                           jackson   949
                       afterburner   949
                          protobuf   258
                     protobuf-json   975
    
    
    with 16 children
             jackson serialization    125.7    136.5    130.8
           jackson deserialization    259.8    283.3    271.3
         afterburner serialization    126.9    140.5    134.2
       afterburner deserialization    256.7    304.2    283.1
            protobuf serialization     19.6     24.9     21.5
          protobuf deserialization     50.7     67.3     58.0
       protobuf-json serialization    719.1    804.9    755.7
     protobuf-json deserialization    621.2    716.7    657.7
    
    encoded sizes:
                           jackson   1804
                       afterburner   1804
                          protobuf   497
                     protobuf-json   1846
    
    
    with 32 children
             jackson serialization    239.6    262.6    250.3
           jackson deserialization    488.5    544.8    518.6
         afterburner serialization    241.3    258.7    249.8
       afterburner deserialization    494.5    533.9    516.8
            protobuf serialization     38.4     50.5     42.9
          protobuf deserialization     97.9    112.5    104.1
       protobuf-json serialization  1,347.1  1,418.8  1,392.6
     protobuf-json deserialization  1,185.7  1,303.8  1,232.5
    
    encoded sizes:
                           jackson   3516
                       afterburner   3516
                          protobuf   977
                     protobuf-json   3590

Example Objects
-----

JSON-encoded object with 1 child (indented for readability): 207 characters

    {
        "num64": 9223372036854775807,
        "flag": true,
        "str": "parent",
        "type": "FOO",
        "children": [
            {
                "num64": 9223372036854775807,
                "flag": true,
                "str": "child 1",
                "type": "FOO",
                "children": null,
                "num32": 2147483647
            }
        ],
        "num32": 2147483647
    }

Protobuf-encoded object with 1 child (byte array): 55 bytes

    { 
      \x08, \x01, \x18, \xff, \xff, \xff, \xff, \x07, \x20, \xff, 
      \xff, \xff, \xff, \xff, \xff, \xff, \xff, \x7f, \x2a, \x06, 
      'p', 'a', 'r', 'e', 'n', 't', \x52, \x1b, \x08, \x01, \x18, 
      \xff, \xff, \xff, \xff, \x07, \x20, \xff, \xff, \xff, \xff, 
      \xff, \xff, \xff, \xff, \x7f, \x2a, \x07, 'c', 'h', 'i', 'l', 
      'd', ' ', '1' 
    }


[article]: http://technicalrex.com/2015/02/27/performance-playground-jackson-vs-protocol-buffers-part-2/
[source]: http://github.com/egillespie/performance-playground 


