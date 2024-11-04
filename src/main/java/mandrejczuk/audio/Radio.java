package mandrejczuk.audio;

public class Radio {


    private String name;
    private String address;
    public Radio(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @Override
    public String toString() {
        return "Radio{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
