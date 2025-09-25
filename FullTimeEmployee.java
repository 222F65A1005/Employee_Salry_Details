package Employee;
class FullTimeEmployee extends Employee {
    private double hra, da;
    public FullTimeEmployee(String name, double basicSalary, double hra, double da) {
        super(name, basicSalary);
        this.hra = hra;
        this.da = da;
    }
    public double calculateSalary() {
        return basicSalary + hra + da;
    }
    public String getType() {
        return "FullTime";
    }
    public double getHra() {return hra;}
    public double getDa() { return da; }
}
