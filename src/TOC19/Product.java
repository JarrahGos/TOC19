package TOC19;

/*
* Author: Jarrah Gosbell
* Student Number: z5012558
* Class: Product
* Description: This program will allow for the input and retreval of products in the product database.
*/

public class Product
{

	// create the variables that are needed in order of use
	private String name;
	private String output;
	private double productPrice;
	private int barCode;
	private int numberOfItems;
	private int quantity;

	public Product(String extName, double extProductPrice, int extBarCode) // construtor which will give the product its values
	{
		output = "";
		name = extName;
		productPrice = extProductPrice;
		barCode = extBarCode;
	}
	// Begin methods
	public void setData(String name, String artist, double productPrice, int barCode) // redundant method which has been left in case products are edited in some way which would require this to be used.
	{
		/**
		Class Song: Method setData
		Precondition: Augments String name, String artist, double productPrice, double time are input
		Postcondition: the product that this method was invoked with now has been stored.
		*/
		this.name = name;
		this.productPrice = productPrice;
		this.barCode = barCode;
	}
	public void setNumber(int number)
	{
		numberOfItems = number;
	}
        public void setName(String name)
        {
            this.name = name;
        }
        public void setPrice(double extPrice)
        {
            this.productPrice = extPrice;
        }
	public int getNumber()
	{
		return numberOfItems;
	}
	public String getData() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		
		output = "";
		output += "\n	Product name: ";
		output += name;
		output += "\n	Bar Code: ";
		output += barCode;
		output += "\n	Price: $";
		output += productPrice;
		output += "\n	Quantity: ";
		output += numberOfItems;

		return output; //return a string with all of the product's data in it
	}
	public String getDataScreen() // output the product data as a string
	{
		/**
		Class Song: Method getData
		Procondition: setData has been run for invoking product or the product constructor outlined above has been run
		Postcondition: The data that has been entered for the invoking product will be returned. 
		*/
		
		output = "";
		output += "\n	Product name: ";
		output += name;
		output += " x ";
		output += quantity;
		output += "\n	Price: $";
		output += productPrice*quantity;
		
		return output; //return a string with all of the product's data in it
	}
	public String getName() // return the name of the product
	{
		/**
		Class Song: Method getData
		Precondition: setData has been run for invoking product
		Post condition: the method will return a string contianing the name.
		*/

		return name;
	}
	public int getBarCode() // return the barcode assoiated with the product
	{
		 /**
		 Class Song: Method getBarCode
		 Precondition: SetData has been run for the invoking product
		 Psotcondition: The method will return an int containing the barcode of the item. 
		 */
		return barCode;
	}
	public double productPrice() // retung the size of the product. 
	{
		/**
		Class Song: Method productPrice
		Precondition: setData has been run for the invoking product
		Postcondition: this method will return the price of the invoking product.
		*/
		
		return productPrice;
	}
	public void decrementNumber()
	{
		numberOfItems--;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public int getQuantity()
	{
		return quantity;
	}
}
