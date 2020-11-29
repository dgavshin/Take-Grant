# Permission
Это класс с уже готовыми экземплярами прав доступа. Не нужно писать ```new Permission("t")```, можно использовать ```Permission.TAKE```

Весь список прав:

	FLOW_READ("im", "fr"),
	FLOW_WRITE("im", "fw"),
	READ("re", "r"),
	WRITE("re", "w"),
	TAKE("re", "t"),
	GRANT("re", "g"),
	ANY("any", "any");
