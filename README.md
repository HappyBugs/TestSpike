简易单机秒杀Core

开发环境：SpringBoot+Maven+MySql+MyBatis+Redis+Jmeter(测试)
实现思路：为了方便开发测试，本地创建订单和用户，在程序启动之后循环对数据库插入100条订单记录，然后在每次请求进来的时候进行用户创建，用于模拟多用户环境。
在秒杀环境下特点就是突然一下并发请求特别大，服务器压力大，为了减轻服务器压力，使用阻塞队列进行保存用户请求，实行先进先出原则(所以网速快就是好)。另外高并发容易导致线程安全问题，所以在controller层关键部分要上锁，避免线程安全问题产生，另外项目秒杀核心代码方法使用线程池进行调用，为的就是提高响应效率，给用户好的使用体验，同时为了减轻服务器的压力，当商品已经秒杀完了的时候在出队的时候进行判断然后拦截返回给用户秒杀失败了，这样就可以避免后面调用秒杀方法，浪费不必要的时间，在秒杀开始之前也就是创建订单的时候就对Redis进行缓存订单号和剩余数量，订单号就用一个list进行保存，每次用户能够抢到商品就在redis中的orders中删除对应的订单号，且对剩余商品个数进行减一，最后根据订单id进行修改数据库，数据库使用存储过程，提高效率。项目很简单，有很多地方以及功能都没实现，知识希望能够帮到跟我一样学习不久的小白。

使用jmeter进行并发测试，我自己最大测试了10000个线程，还是能够进行响应并且数据库也没有出现幂等的情况发生，数据没有从重复消费以及商品没有多卖或者剩余。

运行图：

![image](https://github.com/HappyBugs/TestSpike/blob/master/image/20190525174826.png)
![image](https://github.com/HappyBugs/TestSpike/blob/master/image/20190525174850.png)
![image](https://github.com/HappyBugs/TestSpike/blob/master/image/20190525174900.png)
![image](https://github.com/HappyBugs/TestSpike/blob/master/image/20190525174907.png)
![image](https://github.com/HappyBugs/TestSpike/blob/master/image/20190525174914.jpg)
![image](https://github.com/HappyBugs/TestSpike/blob/master/image/20190525174922.jpg)


