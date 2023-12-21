package jayo.arb.learnj.design_pattern_lib.creational.builder.fruit_product;

import jayo.arb.learnj.design_pattern_lib.creational.singleton.product.MonoRepoSingleton;

import java.util.Objects;

public class Fruit extends Entity {

    public Fruit() {
        super();
    }

    private Fruit( final String id ) {
        super( id );
    }

    public Fruit( Fruit fruit ) {
        super( fruit );
        this.isRipen = fruit.isRipen;
    }

    private Fruit(
            final String id,
            final String name,
            final boolean isRipen
    ) {
        super( id, name );
        this.init( isRipen );
    }

    private void init( final boolean isRipen ) {
        this.setIsRipen( isRipen );
    }

    public void setIsRipen( boolean isRipen ) {
        if( this.isRipen ^ isRipen ) //REM: ...
            this.isRipen = isRipen;
    }

    public boolean isRipen() {
        return this.isRipen;
    }

    @Override
    public String toString() {
        return String.format( "%s",
                super.toString().replace(
                        "]",
                        String.format(", isRipen=%b]", this.isRipen )
                )
        );
    }

    @Override
    public int hashCode() {
        int hashResult = Objects.hash( this.isRipen );
        hashResult = 31 * hashResult + super.hashCode();
        return hashResult;
    }

    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof Fruit) )
            return false;
        if( obj == this )
            return true;
        Fruit f = (Fruit)obj;
        return super.equals( f ) &&
                f.isRipen == this.isRipen;
    }

    public static class FruitBuilder extends Fruit {

        private FruitBuilder() {
            this( "FRUIT-", null, false );
        }

        private FruitBuilder(
                String id,
                String name,
                boolean isRipen
        ) {
            super( id, name, isRipen );
        }

        public static FruitBuilder getInstance() {
            FruitBuilder instance = FruitBuilder.instance;
            if( instance == null ) {
                synchronized( FruitBuilder.class ) {
                    instance = FruitBuilder.instance;
                    if( instance == null )
                        FruitBuilder.instance = instance = new FruitBuilder();
                }
            }
            return instance;
        }

        public FruitBuilder setFruitName( String name ) {
            FruitBuilder.instance.setName( name );
            return this;
        }

        public FruitBuilder setFruitIsRipen( boolean isRipen ) {
            FruitBuilder.instance.setIsRipen( isRipen );
            return this;
        }

        public Fruit build() {
            final FruitBuilder fruitBuilder = FruitBuilder.instance;
            return new Fruit( fruitBuilder.getId()+(id++), fruitBuilder.getName(), fruitBuilder.isRipen() );
        }


        private static int id;
        private static volatile FruitBuilder instance;

    }

    private boolean isRipen;

}
