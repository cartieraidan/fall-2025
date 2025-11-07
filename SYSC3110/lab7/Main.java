public class Main {
    public static void main(String[] args) {
        ResidentialSite residential = new ResidentialSite();
        LifelineSite lifeline = new LifelineSite();

        System.out.println(residential.getBillableAmount());
        System.out.println(lifeline.getBillableAmount());
        System.out.println("refactoring method I used from the blog was control " +
                "T on the child class to pull member up into Site class and alt enter " +
                "on methods called in getBillableAmount()");
    }
}
