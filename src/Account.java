public abstract class Account {
	protected int id;
	protected String name;
	protected int age;
	protected String email;
	protected static int counter = 0;
	protected String username;
	protected String password;
	
	public Account(String name, int age, String email, String username, String password) {
		counter++;
		this.id = counter;
		this.name = name;
		this.age = age;
		this.email = email;
		this.username = username;
		this.password = password;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
