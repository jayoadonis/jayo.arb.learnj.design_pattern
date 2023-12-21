package jayo.arb.learnj.design_pattern_lib.creational.builder.fruit_product.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import jayo.arb.learnj.design_pattern_lib.creational.builder.fruit_product.*;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestFruitBuilder {

    @Test
    @Order( 1 )
    public void testInstance() {

        Fruit apple = Fruit.FruitBuilder.getInstance()
                .setFruitName( "Apple" )
                .setFruitIsRipen( true )
                .build();

        System.out.println( ": " + apple );
        Assertions.assertEquals( "FRUIT-0", apple.getId() );
        Assertions.assertTrue( apple.isRipen() );

        Fruit apple1 = Fruit.FruitBuilder.getInstance()
                .setFruitName( "Apple1" )
                .setFruitIsRipen( false )
                .build();

        System.out.println( ": " + apple1 );
        Assertions.assertTrue( "FRUIT-1".equalsIgnoreCase( apple1.getId() ) );
        Assertions.assertFalse( apple1.isRipen() );

        Fruit apple2 = Fruit.FruitBuilder.getInstance()
                .setFruitName( "Apple2" )
                .setFruitIsRipen( true )
                .build();

        System.out.println( ": " + apple2 );
        Assertions.assertEquals( "fruit-2", apple2.getId().toLowerCase() );
        Assertions.assertTrue( apple2.isRipen() );
    }

    @Test
    @Order( 2 )
    public void testCopyCtor() {

        Fruit apple = Fruit.FruitBuilder.getInstance()
                .setFruitName( "Apple" )
                .setFruitIsRipen( true )
                .build();

        final Fruit apple1 = apple;

        Fruit apple2 = new Fruit( apple1 );

        System.out.println( ":: " + apple );
        System.out.println( ":: " + apple1 );
        System.out.println( ":: " + apple2 );

        System.out.println( ":: " + apple.hashCode() + ", " + apple1.hashCode() );
        System.out.println( ":: " + apple2.hashCode() + ", " + apple1.hashCode() );

        Assertions.assertEquals( "FRUIT-3", apple.getId() );
        Assertions.assertEquals( "FRUIT-3", apple1.getId() );
        Assertions.assertEquals( "FRUIT-3", apple2.getId() );
        Assertions.assertEquals( apple, apple1 );
        Assertions.assertEquals( apple.hashCode(), apple1.hashCode() );
        Assertions.assertEquals( apple2, apple1 );
        Assertions.assertEquals( apple2.hashCode(), apple1.hashCode() );
    }

    @Test
    @Order( 3 )
    public void testFruitDirector() {

        FruitDirector fruitDirector = new FruitDirector();
        Fruit.FruitBuilder fruitBuilder = Fruit.FruitBuilder.getInstance();
        fruitDirector.appleRipen( fruitBuilder );
        Fruit apple = fruitBuilder.build();

        System.out.println( "::: " + apple );
        Assertions.assertEquals( "FRUIT-4", apple.getId() );
        Assertions.assertEquals( "Apple", apple.getName() );
        Assertions.assertTrue( apple.isRipen() );

        fruitDirector.appleUnripen( fruitBuilder );
        Fruit apple1 = fruitBuilder.build();

        System.out.println( "::: " + apple1 );
        Assertions.assertEquals( "FRUIT-5", apple1.getId() );
        Assertions.assertEquals( "Apple", apple1.getName() );
        Assertions.assertFalse( apple1.isRipen() );
    }

}
