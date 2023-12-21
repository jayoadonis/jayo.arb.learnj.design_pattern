package jayo.arb.learnj.design_pattern_lib.creational.builder.fruit_product;

import java.util.Objects;

public abstract class Entity {

    public Entity() {
        this( (String)null );
    }

    protected Entity( String id ) {
        this( id, null );
    }

    protected Entity( String id, String name ) {
        if( id == null || id.isBlank() )
            this.ID = Value.UNKNOWN.VALUE;
        else
            this.ID = id;
        this.init( name );
    }

    protected Entity( Entity entity ) {
        this.ID = entity.ID;
        this.name = entity.name;
    }

    private void init( final String name ) {
        this.setName( name );
    }

    public void setName( String name ) {
        if( name != null &&
                !name.isBlank() &&
                !name.equalsIgnoreCase( Value.UNKNOWN.VALUE )
        ) {
            final String x = name.trim();
            if( this.name == null || !this.name.equalsIgnoreCase( x ) ) {
                this.name = x;
                return;
            }
        }
        if( this.name == null )
            this.name = Value.UNKNOWN.VALUE;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.ID;
    }

    @Override
    public String toString() {
        return String.format( "%s[id='%s', name='%s']",
                super.toString(),
                this.ID, this.name
        );
    }

    @Override
    public int hashCode() {
        int hashResult = Objects.hash( this.ID );
        hashResult = 31 * hashResult + Objects.hash( this.name );
        return hashResult;
    }

    @Override
    public boolean equals( Object obj ) {
        if( !(obj instanceof Entity) )
            return false;
        if( obj == this )
            return true;
        Entity e = (Entity)obj;
        return e.ID.equals( this.ID ) &&
                e.name.equalsIgnoreCase( this.name );
    }

    private final String ID;
    private String name;

}
