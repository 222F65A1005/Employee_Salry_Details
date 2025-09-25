package Employee;

class PartTimeEmployee extends Employee {
    private int hoursWorked;
    private double ratePerHour;

    public PartTimeEmployee(String name, double basicSalary, int hoursWorked, double ratePerHour) {
        super(name, basicSalary);
        this.hoursWorked = hoursWorked;
        this.ratePerHour = ratePerHour;
    }

    @Override
    public double calculateSalary() {
        return basicSalary + (hoursWorked * ratePerHour);
    }

    @Override
    public String getType() {
        return "PartTime";
    }

    public int getHoursWorked() { return hoursWorked; }
    public double getRatePerHour() { return ratePerHour; }
}
