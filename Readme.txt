## Java Exercise: Implement a Bounded Producer Consumer Queue

Implement a Bounded Producer Consumer queue in Java. A producer consumer queue is a queue that multiple producer threads can add data to and multiple consumer threads can read data from concurrently. The queue you implement must be fixed length (bounded) and implement the ProducerConsumerQueue interface provided. The implementation should preferably use generics.

If the queue is full then the enqueue() method must block till the queue is non-full. If the queue is empty then the dequeue() method must block till the queue is non-empty. A more sophisticated implementation that provides various different user configurable strategies for behavior when the queue is full or empty is optional. You must implement the queue data structure using primitives and using the queues provided by the java concurrent api is not a valid solution to the problem.

Finally please also implement a test case that spins up 5 producer threads and 5 consumer threads and exercises the queue with a 1000 elements enqueued and dequeued by each thread. Please provide your working code in java that compiles and runs using Java 8 along with any instructions on how to run your code.

Attention to the details such as code organization, error handling and configuration are important and the final result should be something you'd be proud to ship to customers. (aka. not a prototype.) Extra points for demonstrating your abilities in both design and coding. Ultimately check your code into a public Github repository for review.

Important Note: Please do not use any opensource code (or ready made components available on the web). All code must be completely your own.

We look forward to checking out what you build!
