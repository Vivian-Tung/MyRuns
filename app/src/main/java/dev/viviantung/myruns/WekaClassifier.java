package dev.viviantung.myruns;

class WekaClassifier {

    public static double classify(Object[] i)
            throws Exception {

        double p = Double.NaN;
        p = WekaClassifier.N5e5ee2c70(i);
        return p;
    }
    static double N5e5ee2c70(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 52.704564) {
            p = WekaClassifier.N4dcf09f21(i);
        } else if (((Double) i[0]).doubleValue() > 52.704564) {
            p = WekaClassifier.N7ea5869225(i);
        }
        return p;
    }
    static double N4dcf09f21(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 7.245733) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 7.245733) {
            p = WekaClassifier.N1fb0114b2(i);
        }
        return p;
    }
    static double N1fb0114b2(Object []i) {
        double p = Double.NaN;
        if (i[18] == null) {
            p = 1;
        } else if (((Double) i[18]).doubleValue() <= 0.109091) {
            p = WekaClassifier.N432691f3(i);
        } else if (((Double) i[18]).doubleValue() > 0.109091) {
            p = WekaClassifier.N102e7c2d7(i);
        }
        return p;
    }
    static double N432691f3(Object []i) {
        double p = Double.NaN;
        if (i[18] == null) {
            p = 0;
        } else if (((Double) i[18]).doubleValue() <= 0.051698) {
            p = WekaClassifier.N2d53fb844(i);
        } else if (((Double) i[18]).doubleValue() > 0.051698) {
            p = WekaClassifier.N7c3975b6(i);
        }
        return p;
    }
    static double N2d53fb844(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 22.0461) {
            p = WekaClassifier.N18e6e7455(i);
        } else if (((Double) i[0]).doubleValue() > 22.0461) {
            p = 1;
        }
        return p;
    }
    static double N18e6e7455(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 0.176533) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() > 0.176533) {
            p = 0;
        }
        return p;
    }
    static double N7c3975b6(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 1.09762) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() > 1.09762) {
            p = 0;
        }
        return p;
    }
    static double N102e7c2d7(Object []i) {
        double p = Double.NaN;
        if (i[9] == null) {
            p = 0;
        } else if (((Double) i[9]).doubleValue() <= 1.793158) {
            p = WekaClassifier.N22133a778(i);
        } else if (((Double) i[9]).doubleValue() > 1.793158) {
            p = 0;
        }
        return p;
    }
    static double N22133a778(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 13.463207) {
            p = WekaClassifier.Nde02dbf9(i);
        } else if (((Double) i[0]).doubleValue() > 13.463207) {
            p = WekaClassifier.N4ed59fbe12(i);
        }
        return p;
    }
    static double Nde02dbf9(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 0.250765) {
            p = WekaClassifier.N16821a1710(i);
        } else if (((Double) i[64]).doubleValue() > 0.250765) {
            p = WekaClassifier.N75aec3fe11(i);
        }
        return p;
    }
    static double N16821a1710(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() <= 0.638345) {
            p = 0;
        } else if (((Double) i[2]).doubleValue() > 0.638345) {
            p = 1;
        }
        return p;
    }
    static double N75aec3fe11(Object []i) {
        double p = Double.NaN;
        if (i[22] == null) {
            p = 1;
        } else if (((Double) i[22]).doubleValue() <= 0.017249) {
            p = 1;
        } else if (((Double) i[22]).doubleValue() > 0.017249) {
            p = 0;
        }
        return p;
    }
    static double N4ed59fbe12(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 6.554692) {
            p = WekaClassifier.N4254ce0e13(i);
        } else if (((Double) i[3]).doubleValue() > 6.554692) {
            p = 0;
        }
        return p;
    }
    static double N4254ce0e13(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 0.310942) {
            p = WekaClassifier.N440036f114(i);
        } else if (((Double) i[10]).doubleValue() > 0.310942) {
            p = WekaClassifier.N4732bcbc15(i);
        }
        return p;
    }
    static double N440036f114(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 0.574556) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() > 0.574556) {
            p = 0;
        }
        return p;
    }
    static double N4732bcbc15(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 27.230727) {
            p = WekaClassifier.N25f8e38b16(i);
        } else if (((Double) i[0]).doubleValue() > 27.230727) {
            p = WekaClassifier.N3449b26c20(i);
        }
        return p;
    }
    static double N25f8e38b16(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 0;
        } else if (((Double) i[5]).doubleValue() <= 1.769835) {
            p = WekaClassifier.N41387d8417(i);
        } else if (((Double) i[5]).doubleValue() > 1.769835) {
            p = 0;
        }
        return p;
    }
    static double N41387d8417(Object []i) {
        double p = Double.NaN;
        if (i[19] == null) {
            p = 1;
        } else if (((Double) i[19]).doubleValue() <= 0.480944) {
            p = WekaClassifier.N4ca6cec18(i);
        } else if (((Double) i[19]).doubleValue() > 0.480944) {
            p = 0;
        }
        return p;
    }
    static double N4ca6cec18(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 0.541594) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() > 0.541594) {
            p = WekaClassifier.N564ff79519(i);
        }
        return p;
    }
    static double N564ff79519(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 18.603755) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 18.603755) {
            p = 1;
        }
        return p;
    }
    static double N3449b26c20(Object []i) {
        double p = Double.NaN;
        if (i[27] == null) {
            p = 1;
        } else if (((Double) i[27]).doubleValue() <= 0.424232) {
            p = WekaClassifier.N369f3d3c21(i);
        } else if (((Double) i[27]).doubleValue() > 0.424232) {
            p = WekaClassifier.N48bf2af923(i);
        }
        return p;
    }
    static double N369f3d3c21(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 2.388093) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 2.388093) {
            p = WekaClassifier.N61e59a0f22(i);
        }
        return p;
    }
    static double N61e59a0f22(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 0;
        } else if (((Double) i[13]).doubleValue() <= 0.398995) {
            p = 0;
        } else if (((Double) i[13]).doubleValue() > 0.398995) {
            p = 1;
        }
        return p;
    }
    static double N48bf2af923(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() <= 3.156217) {
            p = 0;
        } else if (((Double) i[3]).doubleValue() > 3.156217) {
            p = WekaClassifier.N311c2b5024(i);
        }
        return p;
    }
    static double N311c2b5024(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() <= 4.730966) {
            p = 1;
        } else if (((Double) i[3]).doubleValue() > 4.730966) {
            p = 0;
        }
        return p;
    }
    static double N7ea5869225(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 1;
        } else if (((Double) i[64]).doubleValue() <= 24.448488) {
            p = WekaClassifier.N335d4eab26(i);
        } else if (((Double) i[64]).doubleValue() > 24.448488) {
            p = WekaClassifier.N4374a9b873(i);
        }
        return p;
    }
    static double N335d4eab26(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 337.573467) {
            p = WekaClassifier.N20442db27(i);
        } else if (((Double) i[0]).doubleValue() > 337.573467) {
            p = WekaClassifier.N23ed778a52(i);
        }
        return p;
    }
    static double N20442db27(Object []i) {
        double p = Double.NaN;
        if (i[14] == null) {
            p = 1;
        } else if (((Double) i[14]).doubleValue() <= 6.68106) {
            p = WekaClassifier.N5292bf6028(i);
        } else if (((Double) i[14]).doubleValue() > 6.68106) {
            p = WekaClassifier.N51d61e2450(i);
        }
        return p;
    }
    static double N5292bf6028(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 114.476379) {
            p = WekaClassifier.Nd132b629(i);
        } else if (((Double) i[0]).doubleValue() > 114.476379) {
            p = WekaClassifier.N633cfadb44(i);
        }
        return p;
    }
    static double Nd132b629(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 8.398658) {
            p = WekaClassifier.N59201c1130(i);
        } else if (((Double) i[5]).doubleValue() > 8.398658) {
            p = WekaClassifier.N18aeae3741(i);
        }
        return p;
    }
    static double N59201c1130(Object []i) {
        double p = Double.NaN;
        if (i[21] == null) {
            p = 1;
        } else if (((Double) i[21]).doubleValue() <= 1.850341) {
            p = WekaClassifier.N79929f7431(i);
        } else if (((Double) i[21]).doubleValue() > 1.850341) {
            p = WekaClassifier.N30bab2b140(i);
        }
        return p;
    }
    static double N79929f7431(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 66.192705) {
            p = WekaClassifier.N3e3c569932(i);
        } else if (((Double) i[0]).doubleValue() > 66.192705) {
            p = WekaClassifier.N6ee8c50134(i);
        }
        return p;
    }
    static double N3e3c569932(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 3.29328) {
            p = WekaClassifier.N3771976a33(i);
        } else if (((Double) i[6]).doubleValue() > 3.29328) {
            p = 0;
        }
        return p;
    }
    static double N3771976a33(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() <= 3.523391) {
            p = 1;
        } else if (((Double) i[7]).doubleValue() > 3.523391) {
            p = 0;
        }
        return p;
    }
    static double N6ee8c50134(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() <= 4.350106) {
            p = 1;
        } else if (((Double) i[5]).doubleValue() > 4.350106) {
            p = WekaClassifier.N3ec9e9735(i);
        }
        return p;
    }
    static double N3ec9e9735(Object []i) {
        double p = Double.NaN;
        if (i[5] == null) {
            p = 0;
        } else if (((Double) i[5]).doubleValue() <= 4.373525) {
            p = 0;
        } else if (((Double) i[5]).doubleValue() > 4.373525) {
            p = WekaClassifier.N5fb888ed36(i);
        }
        return p;
    }
    static double N5fb888ed36(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 1.281977) {
            p = WekaClassifier.N64c99eb737(i);
        } else if (((Double) i[10]).doubleValue() > 1.281977) {
            p = 1;
        }
        return p;
    }
    static double N64c99eb737(Object []i) {
        double p = Double.NaN;
        if (i[16] == null) {
            p = 1;
        } else if (((Double) i[16]).doubleValue() <= 1.583318) {
            p = WekaClassifier.N4c219fe238(i);
        } else if (((Double) i[16]).doubleValue() > 1.583318) {
            p = 3;
        }
        return p;
    }
    static double N4c219fe238(Object []i) {
        double p = Double.NaN;
        if (i[26] == null) {
            p = 1;
        } else if (((Double) i[26]).doubleValue() <= 0.663021) {
            p = 1;
        } else if (((Double) i[26]).doubleValue() > 0.663021) {
            p = WekaClassifier.N2b5ea8d739(i);
        }
        return p;
    }
    static double N2b5ea8d739(Object []i) {
        double p = Double.NaN;
        if (i[22] == null) {
            p = 0;
        } else if (((Double) i[22]).doubleValue() <= 0.937847) {
            p = 0;
        } else if (((Double) i[22]).doubleValue() > 0.937847) {
            p = 1;
        }
        return p;
    }
    static double N30bab2b140(Object []i) {
        double p = Double.NaN;
        if (i[20] == null) {
            p = 0;
        } else if (((Double) i[20]).doubleValue() <= 1.685315) {
            p = 0;
        } else if (((Double) i[20]).doubleValue() > 1.685315) {
            p = 1;
        }
        return p;
    }
    static double N18aeae3741(Object []i) {
        double p = Double.NaN;
        if (i[32] == null) {
            p = 0;
        } else if (((Double) i[32]).doubleValue() <= 0.616347) {
            p = WekaClassifier.N5830fc4f42(i);
        } else if (((Double) i[32]).doubleValue() > 0.616347) {
            p = 1;
        }
        return p;
    }
    static double N5830fc4f42(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 5.473018) {
            p = WekaClassifier.N6f6ff8e43(i);
        } else if (((Double) i[4]).doubleValue() > 5.473018) {
            p = 0;
        }
        return p;
    }
    static double N6f6ff8e43(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 70.334415) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 70.334415) {
            p = 1;
        }
        return p;
    }
    static double N633cfadb44(Object []i) {
        double p = Double.NaN;
        if (i[4] == null) {
            p = 1;
        } else if (((Double) i[4]).doubleValue() <= 11.212689) {
            p = WekaClassifier.Ne8e3f9d45(i);
        } else if (((Double) i[4]).doubleValue() > 11.212689) {
            p = 1;
        }
        return p;
    }
    static double Ne8e3f9d45(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 279.727856) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 279.727856) {
            p = WekaClassifier.N5183688746(i);
        }
        return p;
    }
    static double N5183688746(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 12.781966) {
            p = WekaClassifier.N60b8f7b647(i);
        } else if (((Double) i[6]).doubleValue() > 12.781966) {
            p = 3;
        }
        return p;
    }
    static double N60b8f7b647(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 3;
        } else if (((Double) i[3]).doubleValue() <= 21.864221) {
            p = WekaClassifier.N64d6235848(i);
        } else if (((Double) i[3]).doubleValue() > 21.864221) {
            p = 1;
        }
        return p;
    }
    static double N64d6235848(Object []i) {
        double p = Double.NaN;
        if (i[13] == null) {
            p = 3;
        } else if (((Double) i[13]).doubleValue() <= 1.627738) {
            p = 3;
        } else if (((Double) i[13]).doubleValue() > 1.627738) {
            p = WekaClassifier.N1bdb8a8849(i);
        }
        return p;
    }
    static double N1bdb8a8849(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 284.292605) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 284.292605) {
            p = 3;
        }
        return p;
    }
    static double N51d61e2450(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() <= 168.281125) {
            p = 0;
        } else if (((Double) i[0]).doubleValue() > 168.281125) {
            p = WekaClassifier.N76f446ef51(i);
        }
        return p;
    }
    static double N76f446ef51(Object []i) {
        double p = Double.NaN;
        if (i[22] == null) {
            p = 1;
        } else if (((Double) i[22]).doubleValue() <= 9.019386) {
            p = 1;
        } else if (((Double) i[22]).doubleValue() > 9.019386) {
            p = 0;
        }
        return p;
    }
    static double N23ed778a52(Object []i) {
        double p = Double.NaN;
        if (i[7] == null) {
            p = 3;
        } else if (((Double) i[7]).doubleValue() <= 12.956505) {
            p = WekaClassifier.N64a536b553(i);
        } else if (((Double) i[7]).doubleValue() > 12.956505) {
            p = WekaClassifier.N6a90350b69(i);
        }
        return p;
    }
    static double N64a536b553(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 476.718266) {
            p = WekaClassifier.N5e3333b54(i);
        } else if (((Double) i[0]).doubleValue() > 476.718266) {
            p = WekaClassifier.N37d1800f68(i);
        }
        return p;
    }
    static double N5e3333b54(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 1;
        } else if (((Double) i[2]).doubleValue() <= 28.502769) {
            p = WekaClassifier.N5174de5655(i);
        } else if (((Double) i[2]).doubleValue() > 28.502769) {
            p = WekaClassifier.N3fa745a759(i);
        }
        return p;
    }
    static double N5174de5655(Object []i) {
        double p = Double.NaN;
        if (i[28] == null) {
            p = 2;
        } else if (((Double) i[28]).doubleValue() <= 0.616062) {
            p = 2;
        } else if (((Double) i[28]).doubleValue() > 0.616062) {
            p = WekaClassifier.N196db2d556(i);
        }
        return p;
    }
    static double N196db2d556(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 1;
        } else if (((Double) i[6]).doubleValue() <= 14.661829) {
            p = WekaClassifier.N54b02fd357(i);
        } else if (((Double) i[6]).doubleValue() > 14.661829) {
            p = 2;
        }
        return p;
    }
    static double N54b02fd357(Object []i) {
        double p = Double.NaN;
        if (i[2] == null) {
            p = 3;
        } else if (((Double) i[2]).doubleValue() <= 21.056199) {
            p = WekaClassifier.N4856b49558(i);
        } else if (((Double) i[2]).doubleValue() > 21.056199) {
            p = 1;
        }
        return p;
    }
    static double N4856b49558(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 3;
        } else if (((Double) i[3]).doubleValue() <= 20.949636) {
            p = 3;
        } else if (((Double) i[3]).doubleValue() > 20.949636) {
            p = 1;
        }
        return p;
    }
    static double N3fa745a759(Object []i) {
        double p = Double.NaN;
        if (i[3] == null) {
            p = 3;
        } else if (((Double) i[3]).doubleValue() <= 62.304658) {
            p = WekaClassifier.N4ba8b39c60(i);
        } else if (((Double) i[3]).doubleValue() > 62.304658) {
            p = WekaClassifier.N120ee6a667(i);
        }
        return p;
    }
    static double N4ba8b39c60(Object []i) {
        double p = Double.NaN;
        if (i[64] == null) {
            p = 3;
        } else if (((Double) i[64]).doubleValue() <= 12.043907) {
            p = WekaClassifier.N1eb3c38d61(i);
        } else if (((Double) i[64]).doubleValue() > 12.043907) {
            p = 3;
        }
        return p;
    }
    static double N1eb3c38d61(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 3;
        } else if (((Double) i[0]).doubleValue() <= 406.034608) {
            p = WekaClassifier.N633c3ed262(i);
        } else if (((Double) i[0]).doubleValue() > 406.034608) {
            p = WekaClassifier.N6a6ede2966(i);
        }
        return p;
    }
    static double N633c3ed262(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 2;
        } else if (((Double) i[10]).doubleValue() <= 1.451881) {
            p = WekaClassifier.N6633d56f63(i);
        } else if (((Double) i[10]).doubleValue() > 1.451881) {
            p = WekaClassifier.N6809be6664(i);
        }
        return p;
    }
    static double N6633d56f63(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 367.158536) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 367.158536) {
            p = 3;
        }
        return p;
    }
    static double N6809be6664(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 3;
        } else if (((Double) i[10]).doubleValue() <= 5.478745) {
            p = 3;
        } else if (((Double) i[10]).doubleValue() > 5.478745) {
            p = WekaClassifier.N68a6b74165(i);
        }
        return p;
    }
    static double N68a6b74165(Object []i) {
        double p = Double.NaN;
        if (i[10] == null) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() <= 7.248051) {
            p = 1;
        } else if (((Double) i[10]).doubleValue() > 7.248051) {
            p = 3;
        }
        return p;
    }
    static double N6a6ede2966(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() <= 3.01025) {
            p = 1;
        } else if (((Double) i[11]).doubleValue() > 3.01025) {
            p = 2;
        }
        return p;
    }
    static double N120ee6a667(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() <= 65.996899) {
            p = 3;
        } else if (((Double) i[1]).doubleValue() > 65.996899) {
            p = 1;
        }
        return p;
    }
    static double N37d1800f68(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() <= 521.12939) {
            p = 2;
        } else if (((Double) i[0]).doubleValue() > 521.12939) {
            p = 1;
        }
        return p;
    }
    static double N6a90350b69(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 20.533368) {
            p = WekaClassifier.N761321e670(i);
        } else if (((Double) i[6]).doubleValue() > 20.533368) {
            p = 1;
        }
        return p;
    }
    static double N761321e670(Object []i) {
        double p = Double.NaN;
        if (i[11] == null) {
            p = 2;
        } else if (((Double) i[11]).doubleValue() <= 7.229567) {
            p = WekaClassifier.N13cdd25471(i);
        } else if (((Double) i[11]).doubleValue() > 7.229567) {
            p = 1;
        }
        return p;
    }
    static double N13cdd25471(Object []i) {
        double p = Double.NaN;
        if (i[6] == null) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() <= 15.957698) {
            p = 2;
        } else if (((Double) i[6]).doubleValue() > 15.957698) {
            p = WekaClassifier.N2827f9da72(i);
        }
        return p;
    }
    static double N2827f9da72(Object []i) {
        double p = Double.NaN;
        if (i[1] == null) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() <= 81.947471) {
            p = 1;
        } else if (((Double) i[1]).doubleValue() > 81.947471) {
            p = 2;
        }
        return p;
    }
    static double N4374a9b873(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 871.051925) {
            p = WekaClassifier.N2c07ac2174(i);
        } else if (((Double) i[0]).doubleValue() > 871.051925) {
            p = 2;
        }
        return p;
    }
    static double N2c07ac2174(Object []i) {
        double p = Double.NaN;
        if (i[0] == null) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() <= 655.134028) {
            p = 1;
        } else if (((Double) i[0]).doubleValue() > 655.134028) {
            p = 2;
        }
        return p;
    }
}
