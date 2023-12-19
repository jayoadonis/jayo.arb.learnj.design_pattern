package jayo.arb.learnj.design_pattern_lib.creational.singleton.product;

import java.util.HashSet;
import java.util.Set;

public class MonoRepoSingleton {

    private MonoRepoSingleton( final String URL ) {
        if( URL == null )
            throw new IllegalArgumentException( "URL should not be initially NULL nor EMPTY." );
        this.URL = URL;
        this.repos = new HashSet<String>();
    }

    public static MonoRepoSingleton getInstance( final String URL ) {
        MonoRepoSingleton instance = MonoRepoSingleton.instance;
        if( instance == null ) {
            synchronized( MonoRepoSingleton.class ) {
                instance = MonoRepoSingleton.instance;
                if( instance == null )
                    MonoRepoSingleton.instance = instance = new MonoRepoSingleton( URL );
            }
        }
        return instance;
    }

    public boolean addRepo( final String name ) {
        //REM: TODO-HERE...
        this.repos.add( name );
        return true;
    }

    @Override
    public String toString() {
        return String.format( "%s[size=%d]", super.toString(), this.repos.size() );
    }

    public final Set<String> repos;
    public final String URL;
    private static volatile MonoRepoSingleton instance;
}
