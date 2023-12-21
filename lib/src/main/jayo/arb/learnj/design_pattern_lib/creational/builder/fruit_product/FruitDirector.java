package jayo.arb.learnj.design_pattern_lib.creational.builder.fruit_product;

public class FruitDirector {

    public void appleRipen( Fruit in_out_fruit ) {
        in_out_fruit = Fruit.FruitBuilder.getInstance()
                .setFruitName( "Apple" )
                .setFruitIsRipen( true );
    }

    public void appleUnripen( Fruit in_out_fruit ) {
        in_out_fruit = Fruit.FruitBuilder.getInstance()
                .setFruitName( "Apple" )
                .setFruitIsRipen( false );
    }

}
