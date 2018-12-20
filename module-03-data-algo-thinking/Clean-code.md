# Chaper 14 Successive Refinement


- Chương này cho biết các ví dụ về một module khởi động và chạy tốt nhưng không thể mở rộng được sau đó sẽ là cách tái cấu trúc và làm gọn module.

- Hầu hết chúng ta đều phải lấy các đối số từ command line ,thông thường ta sẽ push hết chúng vào trong hàm main sau đó duyệt lần lượt mảng String chứa các đối số và sau đó là sử dụng chúng. Tuy nhiên ta có thể tạo một class Args để Xử lý việc này.

```
To write clean code, you must first write dirty code and then clean it.Most freshman programmers (like most grade-schoolers) don’t follow this advice par-ticularly well. 
They believe that the primary goal is to get the program working. Once it’s “working,” they move on to the next task, leaving the “working” program in whatever state they finally got it to “work.” 
Most seasoned programmers know that this is professionalsuicide. One of the best ways to ruin a program is to make massive changes to its structure in the name ofnimprovement. 
Some programs never recover from such “improvements.” The problem is that it’s very hard to get the program working the same way it worked before the “improvement.” 
To avoid this, We use the discipline of Test-Driven Development (TDD). One of the cen-tral doctrines of this approach is to keep the system running at all times. 
In other words, using TDD, We are not allowed to make a change to the system that breaks that system. Every change we make must keep the system working as it worked before. 
To achieve this, We need a suite of automated tests that We can run on a whim and that verifies that the behavior of the system is unchanged. 
We could run these tests any time We wanted, and if they passed, We was confident that the system was working as We specified. It is not enough for code to work. 
Code that works is often badly broken. Programmerswho satisfy themselves with merely working code are behaving unprofessionally. 
They may fear that they don’t have time to improve the structure and design of their code, but this is disagreeable. 
Nothing has a more profound and long-term degrading effect upon a develop-ment project than bad code. Bad schedules can be redone, bad requirements can be rede-fined. 
Bad team dynamics can be repaired. But bad code rots and ferments, becoming an
inexorable weight that drags the team down.

```


## Conclusion

Clean code doesn’t come the first time.The first time will be messy. But once you get it working- its your responsibility to make sure that the code is cleaned.
