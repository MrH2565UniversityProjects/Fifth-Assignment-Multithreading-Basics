import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportGenerator {
    static class TaskRunnable implements Runnable {
        private final String path;
        private double totalCost;
        private int totalAmount;
        private int totalDiscountSum;
        private Product mostExpensiveProduct;
        private double highestCostAfterDiscount;

        public TaskRunnable(String path) {
            this.path = path;
            this.totalCost = 0;
            this.totalAmount = 0;
            this.totalDiscountSum = 0;
            this.highestCostAfterDiscount = 0;
            this.mostExpensiveProduct = null;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int Id = Integer.parseInt(parts[0]);
                    Product product = findProductById(Id);
                    if (product == null) continue;
                    int amount = Integer.parseInt(parts[1]);
                    int discount = Integer.parseInt(parts[2]);
                    double price = product.getPrice();
                    double discountedUnitPrice = price * (1 - discount / 100.0);
                    double totalPurchaseCost = discountedUnitPrice * amount;
                    totalAmount += amount;
                    totalDiscountSum += (int) ((price * discount / 100.0) * amount);
                    totalCost += totalPurchaseCost;
                    if (totalPurchaseCost > highestCostAfterDiscount) {
                        highestCostAfterDiscount = totalPurchaseCost;
                        mostExpensiveProduct = product;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Product findProductById(int id) {
                if (id < 10) {
                    return productCatalog[id - 1];
                }
            return null;
        }

        public void makeReport() {
            System.out.println("###### Report for " + path + " ######");
            System.out.printf("Total Cost: $%.2f\n", totalCost);
            System.out.println("Total Items Bought: " + totalAmount);
            double avgDiscount = (totalAmount == 0) ? 0 : (double) totalDiscountSum / totalAmount;
            System.out.printf("Average Discount per item: $%.2f\n", avgDiscount);
            if (mostExpensiveProduct != null) {
                System.out.printf("Most expensive (per unit, after discount): %s - $%.2f\n",
                        mostExpensiveProduct.getProductName(), highestCostAfterDiscount);
            }
            System.out.println("\n\n");
        }
    }

    static class Product {
        private int productID;
        private String productName;
        private double price;

        public Product(int productID, String productName, double price) {
            this.productID = productID;
            this.productName = productName;
            this.price = price;
        }

        public int getProductID() {
            return productID;
        }

        public String getProductName() {
            return productName;
        }

        public double getPrice() {
            return price;
        }
    }
    private static final String[] ORDER_FILES = {
            "src/main/resources/2021_order_details.txt",
            "src/main/resources/2022_order_details.txt",
            "src/main/resources/2023_order_details.txt",
            "src/main/resources/2024_order_details.txt"
    };

    static Product[] productCatalog = new Product[10];

    public static void loadProducts() throws IOException {
        String filename = "src/main/resources/Products.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            int counter = 0;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] cols = line.split(",");
                    productCatalog[counter] = new Product(Integer.parseInt(cols[0]),cols[1],Double.parseDouble(cols[2]));
                    counter++;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        try
        {
            loadProducts();
        }
        catch (IOException e)
        {
            System.out.println("Unable to load Products.txt: " + e.getMessage());
            return;
        }
        List<TaskRunnable> workers = new ArrayList<>();
        List<Thread>       threads = new ArrayList<>();
        for (String filename : ORDER_FILES) {
            TaskRunnable task = new TaskRunnable(filename);
            workers.add(task);
            Thread thread = new Thread(task);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) thread.join();
        for (TaskRunnable worker : workers) worker.makeReport();
    }
}