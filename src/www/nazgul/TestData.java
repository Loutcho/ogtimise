package www.nazgul;

public class TestData {

	public String nom;
	public Source[] source;
	public double volumeObjectif;

	public static final TestData TEST_DATA_METAL;
	static {
		TEST_DATA_METAL = new TestData();
		TEST_DATA_METAL.nom = "Jeu de test METAL";
		TEST_DATA_METAL.volumeObjectif = 3000000.0;
		final int nbSources = 6;
		Source[] source = new Source[nbSources];
		source[0] = new Source(516000.0, 40300.0, 30.0 / 60.0);
		source[1] = new Source(438000.0, 38800.0, 40.0 / 60.0);
		source[2] = new Source(84000.0, 38000.0, 0.0 / 60.0);
		source[3] = new Source(369000.0, 40300.0, 50.0 / 60.0);
		source[4] = new Source(300000.0, 31200.0, 60.0 / 60.0);
		source[5] = new Source(739000.0, 40300.0, 70.0 / 60.0);
		TEST_DATA_METAL.source = source;
	}

	public static final TestData TEST_DATA_CRISTAL;
	static {
		TEST_DATA_CRISTAL = new TestData();
		TEST_DATA_CRISTAL.nom = "Jeu de test CRISTAL";
		TEST_DATA_CRISTAL.volumeObjectif = 1000000.0;
		final int nbSources = 6;
		Source[] source = new Source[nbSources];
		source[0] = new Source(87000.0, 17300.0, 30.0 / 60.0);
		source[1] = new Source(70000.0, 19200.0, 40.0 / 60.0);
		source[2] = new Source(30000.0, 18900.0, 0.0 / 60.0);
		source[3] = new Source(16000.0, 20000.0, 50.0 / 60.0);
		source[4] = new Source(56000.0, 15400.0, 60.0 / 60.0);
		source[5] = new Source(82000.0, 17300.0, 70.0 / 60.0);
		TEST_DATA_CRISTAL.source = source;
	}

	public static final TestData TEST_DATA_DEUTERIUM;
	static {
		TEST_DATA_DEUTERIUM = new TestData();
		TEST_DATA_DEUTERIUM.nom = "Jeu de test DEUTERIUM";
		TEST_DATA_DEUTERIUM.volumeObjectif = 500000.0;
		final int nbSources = 6;
		Source[] source = new Source[nbSources];
		source[0] = new Source(350000.0, 7800.0, 30.0 / 60.0);
		source[1] = new Source(20000.0, 7200.0, 40.0 / 60.0);
		source[2] = new Source(10000.0, 6000.0, 0.0 / 60.0);
		source[3] = new Source(52000.0, 8000.0, 50.0 / 60.0);
		source[4] = new Source(37000.0, 4000.0, 60.0 / 60.0);
		source[5] = new Source(31000.0, 3000.0, 70.0 / 60.0);
		TEST_DATA_DEUTERIUM.source = source;
	}
}
