package jayo.arb.learnj.design_pattern_lib.creational.singleton.product.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import jayo.arb.learnj.design_pattern_lib.creational.singleton.product.MonoRepoSingleton;

import java.util.Arrays;

public class TestMonoRepoSingleton {

    @Test
    public void testInstance() {
        MonoRepoSingleton monoRepoSingleton = MonoRepoSingleton.getInstance( "https://www.sample-repo.com.test" );
        Assertions.assertTrue( monoRepoSingleton.addRepo( "domain.name.project-one" ) );
        Assertions.assertTrue( monoRepoSingleton.addRepo( "domain.name.project-two" ) );

        MonoRepoSingleton monoRepoSingleton2 = MonoRepoSingleton.getInstance( null );
        Assertions.assertTrue( monoRepoSingleton2.addRepo( "domain.name.project-three" ) );

        Assertions.assertEquals( monoRepoSingleton.repos.size(), monoRepoSingleton2.repos.size() );

        System.out.println( monoRepoSingleton );
        System.out.println( monoRepoSingleton2 );
        System.out.println( monoRepoSingleton.repos );
    }
}
