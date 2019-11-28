package xt.influenza;

public class Etat
{
	private double t;
	private int Nm;
	private int Nc;
	private double Sm;
	private double Sc;
	private String sequence;
	
	public Etat() { t  = 0.0; Nm = 0; Nc = 0; Sm = 0.0; Sc = 0.0; sequence = ""; }
	public Etat(double _t, int _Nm, int _Nc, double _Sm, double _Sc, String _sequence)
	{
		t = _t; Nm = _Nm; Nc = _Nc; Sm = _Sm; Sc = _Sc; sequence = _sequence;
	}

	public Etat dup() { return new Etat(t, Nm, Nc, Sm, Sc, sequence); }

	public double Pm() { return Formule.productionHoraire(Ressource.METAL  , Nm); }
	public double Pc() { return Formule.productionHoraire(Ressource.CRISTAL, Nc); }

	public double Kmm() { return Formule.cout(Ressource.METAL  , Ressource.METAL  , Nm); }
	public double Kcm() { return Formule.cout(Ressource.CRISTAL, Ressource.METAL  , Nm); }
	public double Kmc() { return Formule.cout(Ressource.METAL  , Ressource.CRISTAL, Nc); }
	public double Kcc() { return Formule.cout(Ressource.CRISTAL, Ressource.CRISTAL, Nc); }

	public double Dmm() { return (Sm >= Kmm() ? 0.0 : (Kmm() - Sm) / Pm()); }
	public double Dcm() { return (Sc >= Kcm() ? 0.0 : (Kcm() - Sc) / Pc()); }
	public double Dmc() { return (Sm >= Kmc() ? 0.0 : (Kmc() - Sm) / Pm()); }
	public double Dcc() { return (Sc >= Kcc() ? 0.0 : (Kcc() - Sc) / Pc()); }
	
	public double Dm() { return Math.max(Dmm(), Dcm()); }
	public double Dc() { return Math.max(Dmc(), Dcc()); }
	
	public double D1() { return Math.min(Dm(), Dc()); }
	public double D2() { return Math.max(Dm(), Dc()); }
	public int mine1() { return Dm() <= Dc() ? Ressource.METAL : Ressource.CRISTAL; }
	public int mine2() { return Dm() <= Dc() ? Ressource.CRISTAL : Ressource.METAL; }

	public void attendre(double dt) { t += dt; Sm += Pm() * dt; Sc += Pc() * dt; }
	
	public boolean mUpgradable() { return (Sm >= Kmm() && (Sc >= Kcm())); }
	public boolean cUpgradable() { return (Sm >= Kmc() && (Sc >= Kcc())); }

	public void upgraderM() { Sm -= Kmm(); Sc -= Kcm(); Nm = Nm + 1; sequence += "M" + Nm + " "; }
	public void upgraderC() { Sm -= Kmc(); Sc -= Kcc(); Nc = Nc + 1; sequence += "C" + Nc + " "; }
	
	public void upgrader(int mine)
	{
		switch (mine)
		{
		case Ressource.METAL: upgraderM(); break;
		case Ressource.CRISTAL: upgraderC(); break;
		}
	}
	
	public double score() { return Formule.points(Ressource.METAL, Nm) + Formule.points(Ressource.CRISTAL, Nc) + Sm + Sc; }
	
	public String getSequence()
	{
		return sequence;
	}
	
	public void addSequence(String s) { sequence += s; }
	
	public String stat()
	{
		return "" + score() + " : " + sequence;
	}
	
	public String toString2()
	{
		return "" + t + ":" + sequence + ":" + score();
	}

	public String toString()
	{
		String s = "";
		s += "{\n";
		s += "	t = " + t + "\n";
		s += "	Sm = " + Sm + "\n";
		s += "	Sc = " + Sc + "\n";
		s += "	Nm = " + Nm + "\n";
		s += "	Nc = " + Nc + "\n";
		s += "	sequence = " + sequence + "\n";
		s += "}\n";
		return s;
	}
}
