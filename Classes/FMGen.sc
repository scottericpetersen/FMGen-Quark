FMGen {

	var definitions;
	var carrierF, middleF, modulatorF;
	var mod1, mod2, mod3;
	var mid1, mid2, mid3;
	var car1, car2, car3;
	var rnd1, rnd2, rnd3;
	var env1, env2, env3;
	var m1m, m2m, m3m;
	var m1d, m2d;
	var m1c, m2c, m3c, m4c;
	var phase1, phase2, phase3;
	var elvl1, elvl2, elvl3, e2lvl1, e3lvl1, e4lvl1;
	var mix1=0, mix2=0, mix3=0;
	var m1e1v1, m1e1v2, m2e1v1, m2e1v2, m3e1v1, m3e1v2;
	var m1de1v1, m1de1v2, m2de1v1, m2de1v2;
	var c1e1v1, c1e1v2, c2e1v1, c2e1v2, c3e1v1, c3e1v2, c4e1v1, c4e1v2;
	var c1e1l, c2e1l, c3e1l, c4e1l;
	var date, path, sdf, anl;
	var freq=440, amp=1, gate=1;

	var ai=0, bi=0, ci=0;

	*new {|num_defs=10, max_attack=0.4, max_release=3, fm_type=0|
		^super.new.generate(num_defs, max_attack, max_release, fm_type);
	}

	generate {|num_defs, max_attack, max_release, fm_type|

		definitions = Dictionary.new();
		date =  Date.getDate.dayStamp;
		date.removeAt(date.size - 1);
		// Create the folder synthdef-files in the same dir as this doc.
		path = ("fmgen-defs/fmgen_synthdefs_" ++ date ++ ".scd").resolveRelative;
		sdf = File.open(path, "w+");

		// X.do determines the number of random synthdefs generated
		num_defs.do { |i|
			var name, defname, sd, array;
			name = "fmgen";
			array = [
				{  /* Category A: Three (3) Summed (parallel) CM pairs. */

					ai = ai + 1;
					name = name ++ "_a_" ++ ai;
					[\A, i].postln;

					// Random Generation Round 1:
					rnd1 = 1.8.rand2.squared;
					m1m = linrand(10) + 1;
					m1c = linrand(5) + 1;
					phase1 = 1.3.rand.cubed;
					elvl1 = 3.0.rand.squared;
					m1e1v1 = exprand(0.001, max_attack);
					m1e1v2 = exprand(0.1, max_release);
					c1e1v1 = exprand(0.001, max_attack);
					c1e1v2 =  exprand(0.1, max_release);
					c1e1l = rrand(0.5,0.6).rand.squared;

					// Random Generation Round 2:
					rnd2 = 1.8.rand2.squared;
					m2m = linrand(5) + 1;
					m2c = linrand(10) + 1;
					phase2 = 1.3.rand.cubed;
					elvl2 = 3.0.rand.squared;
					m2e1v1 = exprand(0.001, max_attack);
					m2e1v2 = exprand(0.1, max_release);
					c2e1v1 = exprand(0.001, max_attack);
					c2e1v2 =  exprand(0.1, max_release);
					c2e1l = rrand(0.5,0.6).rand.squared;

					// Random Generation Round 3:
					rnd3 = 1.8.rand2.squared;
					m3m = linrand(5) + 1;
					m3c = linrand(10) + 1;
					phase3 = 1.3.rand.cubed;
					elvl3 = 3.0.rand.squared;
					m3e1v1 = exprand(0.001, max_attack);
					m3e1v2 = exprand(0.1, max_release);
					c3e1v1 = exprand(0.001, max_attack);
					c3e1v2 =  exprand(0.1, max_release);
					c3e1l = rrand(0.5,0.6).rand.squared;

					// Post some information about the CM ratios and index and write the results to the synthdef file.
					defname = name.asSymbol.asCompileString; // without .asSymbol doesn't get interpreted correctly

					anl = "/*___________________________________________________\n" + "\n" +
					"Results:" + "SynthDef" + defname + "parameters: \n"
					"___________________________________________________\n\n" +

					"CM Pair 1 Ratio = 1 :" + (m1m / m1c).round(0.01) ++ ", Index of Modulation: " + elvl1.round(0.010) + "\n" +
					"CM Pair 2 Ratio = 1 :" + (m2m / m2c).round(0.01) ++ ", Index of Modulation: " + elvl2.round(0.010) + "\n" +
					"CM Pair 3 Ratio = 1 :" + (m3m / m3c).round(0.01) ++ ", Index of Modulation: " + elvl3.round(0.010) + "\n" + "*/";
					anl.postln;
					sdf.write(anl + "\n\n");

					// I added a Select.ar() object and argument to make auditioning the CM pairs easier
					sd = "SynthDef(" + defname + ", { arg freq=440, amp=1, gate=1, pan=0, prs=0; \n" +
					"var out, env1, env2, env3, mod1, mod2, mod3, car1, car2, car3, slct;
env1 = Env.perc(" + m1e1v1 + "," + m1e1v2 + ");
env1 = EnvGen.kr(env1, gate, " + elvl1 + ");
mod1 = SinOsc.ar(freq + " + rnd1 + " * " + m1m + ", " + phase1 + ", env1);
env1 = Env.perc(" + c1e1v1 + ", " + c1e1v2 + ");
env1 = EnvGen.kr(env1, gate," + c1e1l + ");
car1 = SinOsc.ar(freq +" + rnd1 + " * " + m1c + ", mod1, env1);

env2 = Env.perc(" + m2e1v1 + "," + m2e1v2 + ");
env2 = EnvGen.kr(env2, gate, " + elvl2 + ");
mod2 = SinOsc.ar(freq + " + rnd2 + " * " + m2m + ", " + phase2 + ", env2);
env2 = Env.perc(" + c2e1v1 + ", " + c2e1v2 + ");
env2 = EnvGen.kr(env2, gate," + c2e1l + ");
car2 = SinOsc.ar(freq +" + rnd2 + " * " + m2c + ", mod2, env2);

env3 = Env.perc(" + m3e1v1 + "," + m3e1v2 + ");
env3 = EnvGen.kr(env3, gate, " + elvl3 + ");
mod3 = SinOsc.ar(freq + " + rnd3 + " * " + m3m + ", " + phase3 + ", env3);
env3 = Env.perc(" + c3e1v1 + ", " + c3e1v2 + ");
env3 = EnvGen.kr(env3, gate," + c3e1l + ");
car3 = SinOsc.ar(freq +" + rnd3 + " * " + m3c + ", mod3, env3);
out = car1 + car2 + car3;

slct = Select.ar(prs, [out, car1, car2, car3]);

DetectSilence.ar(slct, doneAction: 2);
Out.ar(0, Pan2.ar(slct, pan, amp));
}).add;";

					definitions.add(name.asSymbol -> sd);
					sd.postln;
					sdf.write(sd + "\n\n");
				}
				,
				{
					/* Sum of two modulator->modulator->carrier chains.
					(Series Multi-modulator FM) */

					bi = bi + 1;
					name = name ++ "_b_" ++ bi;
					[\B, i].postln;

					// Random Generation Round 1:

					rnd1 = 1.8.rand2.squared; // random add for frequencies
					m1m = linrand(10) + 1; // modulator ratio
					m1d = linrand(5) + 1; // mid-modulator ratio
					m1c = linrand(5) + 1; // carrier ratio
					phase1 = 1.3.rand.cubed; // phase for modulator
					elvl1 = 3.0.rand.squared; // IM for modulator
					e2lvl1 = 3.0.rand.squared; // IM for mid-modulator
					m1e1v1 = exprand(0.001,max_attack); // mod env atk
					m1e1v2 = exprand(0.1, max_release);  // mod env release
					m1de1v1 = exprand(0.001,max_attack); //mid-mod env atk
					m1de1v2 = exprand(0.1, max_release); // mid-mod env release
					c1e1v1 = exprand(0.001,max_attack); // car env atk
					c1e1v2 =  exprand(0.1, max_release); // car env release
					c1e1l = rrand(0.5,0.6).rand.squared; // car IM

					// Random Generation Round 2:

					rnd2 = 1.8.rand2.squared; // random add for frequencies
					m2m = linrand(10) + 1; // modulator ratio
					m2d = linrand(5) + 1; // mid-modulator ratio
					m2c = linrand(5) + 1; // carrier ratio
					phase2 = 1.3.rand.cubed; // phase for modulator
					e3lvl1 = 3.0.rand.squared; // IM for modulator
					e4lvl1 = 3.0.rand.squared; // IM for mid-modulator
					m2e1v1 = exprand(0.001, max_attack); // mod env atk
					m2e1v2 = exprand(0.1, max_release);  // mod env release
					m2de1v1 = exprand(0.001, max_attack); //mid-mod env atk
					m2de1v2 = exprand(0.1, max_release); // mid-mod env release
					c2e1v1 = exprand(0.001, max_attack); // car env atk
					c2e1v2 =  exprand(0.1, max_release); // car env release
					c2e1l = rrand(0.5,0.6).rand.squared; // car IM

					defname = name.asSymbol.asCompileString; // without .asSymbol doesn't get interpreted correctly

					anl = "/*___________________________________________________\n" + "\n" +
					"Results:" + "SynthDef" + defname + "parameters: \n"
					"___________________________________________________\n\n" +

					"Set 1 C:M1:M2 Ratios = 1 : " ++ (m1m / m1c).round(0.01) ++ " : " ++  (m1d / m1c).round(0.01) + ", Indices of Modulation: M1: " ++ elvl1.round(0.010) ++ "M2:" ++ e2lvl1 ++ "\n" +
					"Set 2 C:M1:M2 Ratios = 1 : " ++ (m2m / m2c).round(0.01) ++ " : " ++  (m2d / m2c).round(0.01) + ", Indices of Modulation: M1: " ++ e3lvl1.round(0.010) ++ "M2:" ++ e4lvl1 ++ "\n" +
					"*/";
					anl.postln;
					sdf.write(anl + "\n\n");

					// I added a Select.ar() object and argument to make auditioning the CM pairs easier
					sd = "SynthDef(" + defname + ", { arg freq=440, amp=1, gate=1, pan=0, prs=0; \n" +
					"var out, env1, env2, env3, env4, env5, env6, mod1, mod2, mod3, mod4, car1, car2, slct;
env1 = Env.perc(" + m1e1v1 + "," + m1e1v2 + ");
env1 = EnvGen.kr(env1, gate, " + elvl1 + ");
mod1 = SinOsc.ar(freq + " + rnd1 + " * " + m1m + ", " + phase1 + ", env1);

env2 = Env.perc(" + m1de1v1 + "," + m1de1v2 + ");
env2 = EnvGen.kr(env2, gate, " + e2lvl1 + ");
mod2 = SinOsc.ar(freq + " + rnd1 + " * " + m1d + ", mod1, env2);

env3 = Env.perc(" + c1e1v1 + ", " + c1e1v2 + ");
env3 = EnvGen.kr(env3, gate," + c1e1l + ");
car1 = SinOsc.ar(freq +" + rnd1 + " * " + m1c + ", mod2, env3);

env4 = Env.perc(" + m2e1v1 + "," + m2e1v2 + ");
env4 = EnvGen.kr(env4, gate, " + e3lvl1 + ");
mod3 = SinOsc.ar(freq + " + rnd2 + " * " + m2m + ", " + phase2 + ", env4);

env5 = Env.perc(" + m2de1v1 + "," + m2de1v2 + ");
env5 = EnvGen.kr(env5, gate, " + e4lvl1 + ");
mod4 = SinOsc.ar(freq + " + rnd2 + " * " + m2d + ", mod3, env5);

env6 = Env.perc(" + c2e1v1 + ", " + c2e1v2 + ");
env6 = EnvGen.kr(env6, gate," + c2e1l + ");
car2 = SinOsc.ar(freq +" + rnd2 + " * " + m2c + ", mod4, env6);

out = car1 + car2;

slct = Select.ar(prs, [out, car1, car2]);

DetectSilence.ar(slct, doneAction: 2);
Out.ar(0, Pan2.ar(slct, pan, amp));
}).add;";

					definitions.add(name.asSymbol -> sd);
					sd.postln;
					sdf.write(sd + "\n\n");
				} ,
				{
					/* Sum of 2 modulator-+->carrier
					|
					+->carrier
					(Parellel Multi-carrier FM) */

					ci = ci + 1;
					name = name ++ "_c_" ++ ci;
					[\C, i].postln;

					// Random Generation Round 1:
					rnd1 = 1.8.rand2.squared; // random add for frequencies
					m1m = linrand(10) + 1; // modulator ratio
					m1c = linrand(5) + 1; // carrier 1 ratio
					m2c = linrand(5) + 1; // carrier 2 ratio
					phase1 = 1.3.rand.cubed; // phase for modulator
					elvl1 = 3.0.rand.squared; // IM for modulator
					m1e1v1 = exprand(0.001,max_attack); // mod env atk
					m1e1v2 = exprand(0.1, max_release); // mod env release
					c1e1v1 = exprand(0.001,max_attack); // car env atk
					c1e1v2 =  exprand(0.1, max_release); // car env release
					c1e1l = rrand(0.5,0.6).rand.squared; // car 1 IM
					c2e1v1 = exprand(0.001,max_attack); // car 2 env atk
					c2e1v2 =  exprand(0.1, max_release); // car 2 env release
					c2e1l = rrand(0.5,0.6).rand.squared;// car 2 IM

					// Random Generation Round 2:
					rnd2 = 1.8.rand2.squared;// random add for frequencies
					m2m = linrand(5) + 1;// modulator ratio
					m3c = linrand(10) + 1;
					m4c = linrand(10) + 1;
					phase2 = 1.3.rand.cubed;
					elvl2 = 3.0.rand.squared;
					m2e1v1 = exprand(0.001,max_attack);
					m2e1v2 = exprand(0.1, max_release);
					c3e1v1 = exprand(0.001,max_attack); // car 3 env atk
					c3e1v2 =  exprand(0.1, max_release); // car 3 env release
					c3e1l = rrand(0.5,0.6).rand.squared; // car 3 IM
					c4e1v1 = exprand(0.001,max_attack); // car 4 env atk
					c4e1v2 =  exprand(0.1, max_release); // car 4 env release
					c4e1l = rrand(0.5,0.6).rand.squared;// car 4 IM


					// Post some information about the CM ratios and index and write the results to the synthdef file.
					defname = name.asSymbol.asCompileString; // without .asSymbol doesn't get interpreted correctly

					anl = "/*___________________________________________________\n" + "\n" +
					"Results:" + "SynthDef" + defname + "parameters: \n"
					"___________________________________________________\n\n" +

					"C1:C2:M1 Ratios: 1 :" + (m2c / m1c).round(0.01) + ":" + (m1m / m1c).round(0.01) + ", Index of Modulation: " + elvl1.round(0.010) + "\n" +
					"C3:C4:M2 Ratios: 1 :" + (m4c / m3c).round(0.01) + ":" + (m2m / m3c).round(0.01) + ", Index of Modulation: " + elvl2.round(0.010) + "\n" +
					"*/";

					anl.postln;
					sdf.write(anl + "\n\n");

					// I added a Select.ar() object and argument to make auditioning the CM pairs easier
					sd = "SynthDef(" + defname + ", { arg freq=440, amp=1, gate=1, pan=0, prs=0; \n" +
					"var out, env1, env2, env3, env4, env5, env6, mod1, mod2, car1, car2, car3, car4, slct;
env1 = Env.perc(" + m1e1v1 + "," + m1e1v2 + ");
env1 = EnvGen.kr(env1, gate, " + elvl1 + ");
mod1 = SinOsc.ar(freq + " + rnd1 + " * " + m1m + ", " + phase1 + ", env1);
env2 = Env.perc(" + c1e1v1 + ", " + c1e1v2 + ");
env2 = EnvGen.kr(env2, gate," + c1e1l + ");
car1 = SinOsc.ar(freq +" + rnd1 + " * " + m1c + ", mod1, env2);
env3 = Env.perc(" + c2e1v1 + ", " + c2e1v2 + ");
env3 = EnvGen.kr(env3, gate," + c2e1l + ");
car2 = SinOsc.ar(freq +" + rnd1 + " * " + m2c + ", mod1, env3);


env4 = Env.perc(" + m2e1v1 + "," + m2e1v2 + ");
env4 = EnvGen.kr(env4, gate, " + elvl2 + ");
mod2 = SinOsc.ar(freq + " + rnd2 + " * " + m2m + ", " + phase2 + ", env4);
env5 = Env.perc(" + c3e1v1 + ", " + c3e1v2 + ");
env5 = EnvGen.kr(env5, gate," + c3e1l + ");
car3 = SinOsc.ar(freq +" + rnd2 + " * " + m3c + ", mod2, env5);
env6 = Env.perc(" + c4e1v1 + ", " + c4e1v2 + ");
env6 = EnvGen.kr(env6, gate," + c4e1l + ");
car4 = SinOsc.ar(freq +" + rnd2 + " * " + m4c + ", mod2, env6);

out = car1 + car2;

slct = Select.ar(prs, [out, car1, car2]);

DetectSilence.ar(slct, doneAction: 2);
Out.ar(0, Pan2.ar(slct, pan, amp));
}).add;";

					definitions.add(name.asSymbol -> sd);
					// Post the synthdef
					sd.postln;
					// Write the synthdef to a file
					sdf.write(sd + "\n\n");

				}
			];

			case {fm_type == 0 } { array.choose.() } // random choice, A, B, or C
			{fm_type == 1 } { array[0].() } // choose only type A: 3 CM pairs
			{fm_type == 2 } { array[1].() } // choose only type B: 2 MMC trips
			{fm_type == 3 } { array[2].() } // choose only type C: 2 MCC trips
		};

		sdf.close; // close the synthdefs file
		path.load; // read all synthdefs written to the file for testing/evaluation
		"\n".postln;
		("Highest Category A Synth: " ++ ai).postln;
		("Highest Category B Synth: " ++ bi).postln;
		("Highest Category C Synth: " ++ ci).postln;
	}

	defs {|which|

		if (which == \all) { definitions.postcs} {definitions[which].postln  };
	}

	play {|which=\all, num_notes=10, root_note=36, scale=\minor, num_octaves=2|

		var synths = definitions.keys.asArray.sort;
		scale = ("Scale." ++ scale.asString ++ ".degrees").interpret;

		if (which == \all ) {
			Routine({

				var octs_scale = num_octaves.do {|i| i = i + 1; scale = scale ++ scale * i };
				var octGen = { root_note + scale };
				var oct = octGen.();
				var freq;

				synths.do {|name|
					var array, freq, id;
					("SynthDef: " ++ name).postln;

					num_notes.do {

						freq = oct.choose.midicps.postln;
						Synth(name, [\freq, freq, \amp, 0.8, \pan, 1.0.rand2]);

						[0.1, 0.2, 0.4, 0.8, 1.6].wchoose([3,2,1,1,1].normalizeSum).wait;
					};
				};

			}).play;
		}
		{ // Play only one synthdef specified by name
			Routine({

				var octs_scale = num_octaves.do {|i| i = i + 1; scale = scale ++ scale * i };
				var octGen = { root_note + scale };
				var oct = octGen.();
				var freq;

				num_notes.do {

					freq = oct.choose.midicps.postln;
					Synth(which, [\freq, freq, \amp, 0.8, \pan, 1.0.rand2]);

					[0.1, 0.2, 0.4, 0.8, 1.6].wchoose([3,2,1,1,1].normalizeSum).wait;
				};


			}).play;

		}

	}

	save {|def_name, description=""|

		if (definitions[def_name].notNil)
		{
			Dialog.savePanel({ arg path;
				File.open(path, "w+").write("/*" + description + "\n" + "*/" + "\n" + definitions[def_name]).close;
				("SynthDef Plain Text Written to file:" + path).postln;
			},{
				"cancelled".postln;
			}
			); // end file dialog
		}
		{ "SynthDef does not exist. Please check your def name and try again.".warn} // false: warn no definition of that name

	}

}
