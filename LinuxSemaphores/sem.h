/* Task Queue node structure, for queueing tasks */
struct task_node{
	struct task_struct* curTask;
	struct task_node* next;
};

/* Semaphore data type, storing the key value and the head, tail of the FIFO queue*/
struct cs1550_sem{
	int value;
	struct task_node* head;
	struct task_node* tail;
};
