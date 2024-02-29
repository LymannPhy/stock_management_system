package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/*class FileReadWriteExample {
    public static void main(String[] args) {
        // Paths to source and target files
        String sourceFilePath = "source.txt";
        String targetFilePath = "target.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(sourceFilePath));
                BufferedWriter writer = new BufferedWriter(new FileWriter(targetFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("File copied successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}*/


// method random
// choice 2, write 1M = 0.93
   /*public static void randomWrite() {
        System.out.print("> Enter random amount = ");
        int amount1 = scanner.nextInt();
        Thread thread1 = new Thread(() -> {
            amount.set(amount1);
            System.out.print("> Are you sure to random " + amount + " products? [y/n]: ");
            scanner.nextLine(); // Consume the newline left-over
            String save = scanner.nextLine();
            if (Objects.equals(save, "y")) {
                System.out.println("Waiting....");

                long startTime = System.nanoTime(); // Start time
                try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat"))) {
                    for (int i =   0; i < amount1; i++) {
                        j.incrementAndGet();
                        Product newProduct = new Product("CSTAD" + (i +   1), "Product" + (i +   1),   100.00d,   10, LocalDate.now().toString());
                        String serializedProduct = serializeProduct(newProduct);
                        writer.write(serializedProduct);
                        writer.newLine(); // Ensure each product is written on a new line
                    }
                    System.out.println(j);
                } catch (IOException e) {
                    System.err.println("Error writing to transaction file: " + e.getMessage());
                }

                System.out.println(amount + " Product(s) created successfully.");
                long endTime = System.nanoTime(); // End time
                long resultTime = endTime - startTime;
                System.out.println("Writing " + amount + " products spent: " + (resultTime /  1_000_000_000.0) + " seconds.");
            }
        });
        Thread thread2 = new Thread(() -> {
            try {
                thread1.join(); // Wait for thread1 to complete
                while (j.get() != amount.get()) {
                    int progress = (int) (((double) j.get() / amount.get()) *  100);
                    System.out.print("\rLoading [" + progress + "%] ");
                }
                System.out.println("\nThread  2: j = " + j + ", amount = " + amount);
            } catch (InterruptedException e) {
                System.out.println("Thread  2 was interrupted: " + e.getMessage());
            }
        });
        thread1.start();
        //thread2.start();
        try {
            thread1.join();
            //thread2.join();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // choice 3
    /*public static void randomWrite() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("> Enter random amount = ");
        int amount = scanner.nextInt();
        System.out.print("> Are you sure to random " + amount + " products? [y/n]: ");
        scanner.nextLine(); // Consume newline left-over
        String save = scanner.nextLine();
        if (Objects.equals(save, "y")) {
            long startTime = System.nanoTime();

            List<String> serializedProducts = new ArrayList<>();
            for (int i =  0; i < amount; i++) {
                Product newProduct = new Product("CSTAD" + (i +  1), "Product" + (i +  1),  100.00d,  10, LocalDate.now().toString());
                products.add(newProduct);
                serializedProducts.add(serializeProduct(newProduct));
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/transaction.dat"))) {
                int size = serializedProducts.size();
                for (int i =  0; i < size; i++) {
                    writer.write(serializedProducts.get(i) + "\n");
                }
            } catch (IOException e) {
                System.err.println("Error writing to transaction file: " + e.getMessage());
            }
            System.out.println(amount + " Product(s) created successfully.");
            long endTime = System.nanoTime();
            long resultTime = endTime - startTime;
            System.out.println("Write " + amount + " products spend : " + (resultTime /  1_000_000_000.0) + " seconds.");
        }
    }*/


/*public void test(){
    for (int i = 0; i <= 100; i+=2) {
        int totalBlocks = 50;
        int blocksToShow = (i * totalBlocks) / 100;
        System.out.print(" ".repeat(20) + " Loading [ " + i + "% ]");
        System.out.print(" ".repeat(10) + getProgressBar(blocksToShow, totalBlocks) + "\r");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}*/

/*private static String getProgressBar(int blocksToShow, int totalBlocks) {
        String progressBar = "█".repeat(Math.max(0, blocksToShow)) +
                " ".repeat(Math.max(0, totalBlocks - blocksToShow));
        return green + progressBar;
    }*/


/*public void random(){
        while (true){
            System.out.println("1. Write");
            System.out.println("2. Read");
            System.out.println("3. Back");
            System.out.print("Choose : ");
            int op = scanner.nextInt();
            switch (op){
                case 1 -> randomWrite();
                case 2 ->
                case 3 -> {
                    return;
                }
            }
        }
    }*/

/*public void backUp(){
        String orgPath  = "data/product.dat";
        String copyPath = "data/backup.dat";
        try (FileInputStream in = new FileInputStream(orgPath);
             FileOutputStream out = new FileOutputStream(copyPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Backup successfully!");
        } catch (IOException e) {e.printStackTrace();}
    }

    public void restore(){
        deleteFile();
        String orgPath = "data/backup.dat";
        String copyPath  = "data/product.dat";
        try (FileInputStream in = new FileInputStream(orgPath);
             FileOutputStream out = new FileOutputStream(copyPath)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            System.out.println("Restore successfully!");
        } catch (IOException e) {e.printStackTrace();}
    }

    public static void deleteFile(){
        Path path = Paths.get("data/product.dat");
        try {
            Files.delete(path);
            //System.out.println("File deleted successfully.");
        } catch (IOException e) {
            System.out.println("Failed to delete the file.");
            e.printStackTrace();
        }
    }*/

/*public static Integer generateCodeFromFile(){
        String filePath = "data/code_count.txt";
        int code = 0;
        try {
            String data = Files.readString(Paths.get(filePath)).trim();
            code = Integer.parseInt(data);
            //System.out.println("The integer read from the file is: " + code);
        } catch (IOException e) {
            System.err.println("An error occurred while reading the file.");
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("The file does not contain a valid integer.");
            e.printStackTrace();
        }
        return code;
    }

    public static void codeToFile(){
        String filePath = "data/code_count.txt";
        int data = generateCodeFromFile();
        //int data = 10;
        String line = String.valueOf(data);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(line);
            //System.out.println("Data written to the file successfully.");
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
        }
    }

    public void randomWrite() {
        System.out.print("> Enter random amount = ");
        int amount = scanner.nextInt();
        amountProduct = amount;
        System.out.print("> Are you sure to random " + amount + " products? [y/n]: ");
        scanner.nextLine();
        int sum=0;
        String save = scanner.nextLine();
        if (Objects.equals(save, "y")) {
            //String codeA = generateProductCode(); // 3
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/product.dat",true))) {
                long startTime = System.nanoTime(); // Start time
                int i = 0;
                int code = generateCodeFromFile();
                while (i < amount) {
                    Product newProduct = new Product("CSTAD-"+ (i+1), "P" + (i +  1), 10.00d, 10, LocalDate.now().toString());
                    String serializedProduct = serializeProduct(newProduct);
                    writer.write(serializedProduct);
                    writer.newLine();
                    if ( (amount > 10000) && (i + 1) % (amount / 10) == 0 || i == amount - 1) {
                        int progress = ((i + 1) * 100) / amount;
                        System.out.print("\rLoading[" + progress + "%]");
                    }
                    i++;
                    code++;
                    sum+=i;
                }
                System.out.println("\r"+amount + " Products created successfully.");
                long endTime = System.nanoTime(); // End time
                long resultTime = endTime - startTime;
                System.out.println("Writing " + amount + " products spent: " + (resultTime /  1_000_000_000.0) + " seconds.");

            } catch (IOException e) {
                System.err.println("Error writing to transaction file: " + e.getMessage());
            }
            String filePath = "data/code_count.txt";
            int data = generateCodeFromFile();
            int temp = data + sum;
            //int data = 10;
            String line = String.valueOf(temp);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                writer.write(line);
                //System.out.println("Data written to the file successfully.");
            } catch (IOException e) {
                System.err.println("An error occurred while writing to the file: " + e.getMessage());
            }
        }
    }*/