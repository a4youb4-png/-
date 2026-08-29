package com.example.data.repository

import com.example.data.model.DhikrCategory
import com.example.data.model.DhikrItem
import com.example.data.model.DuaCategory
import com.example.data.model.DuaItem
import com.example.data.model.TasbihItem

object DhikrRepository {

    val ayatAlKursiItem = DhikrItem(
        id = "ayat_al_kursi_main",
        category = DhikrCategory.POST_PRAYER,
        textArabic = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَنْ ذَا الَّذِي يَشْفَعُ عِنْدَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ.",
        translation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence.",
        countTarget = 1,
        benefit = "من قرأها دبر كل صلاة مكتوبة لم يمنعه من دخول الجنة إلا أن يموت",
        sourceCitation = "سورة البقرة: 255 (رواه النسائي وصححه الألباني)",
        audioUrl = "https://server8.mp3quran.net/afs/002.mp3",
        isAyatAlKursi = true
    )

    fun getPostPrayerDhikrs(): List<DhikrItem> {
        return listOf(
            DhikrItem(
                id = "post_1",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "أَسْتَغْفِرُ اللَّهَ",
                countTarget = 3,
                benefit = "الاستغفار ثلاثاً عقب الصلاة استدراكاً لما قد يحصل من تقصير",
                sourceCitation = "صحيح مسلم (591)"
            ),
            DhikrItem(
                id = "post_2",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "اللَّهُمَّ أَنْتَ السَّلَامُ، وَمِنْكَ السَّلَامُ، تَبَارَكْتَ يَا ذَا الْجَلَالِ وَالإِكْرَامِ.",
                countTarget = 1,
                benefit = "سؤال الله السلامة والبركة عقب السلام من الصلاة",
                sourceCitation = "صحيح مسلم (591)"
            ),
            DhikrItem(
                id = "post_3",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، اللَّهُمَّ لَا مَانِعَ لِمَا أَعْطَيْتَ، وَلَا مُعْطِيَ لِمَا مَنَعْتَ، وَلَا يَنْفَعُ ذَا الْجَدِّ مِنْكَ الْجَدُّ.",
                countTarget = 1,
                benefit = "توحيد الله وإثبات القدرة المطلقة وإفراده بالمنع والعطاء",
                sourceCitation = "صحيح البخاري (844) وصحيح مسلم (593)"
            ),
            DhikrItem(
                id = "post_4",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ، لَا إِلَهَ إِلَّا اللَّهُ، وَلَا نَعْبُدُ إِلَّا إِيَّاهُ، لَهُ النِّعْمَةُ وَلَهُ الْفَضْلُ وَلَهُ الثَّنَاءُ الْحَسَنُ، لَا إِلَهَ إِلَّا اللَّهُ مُخْلِصِينَ لَهُ الدِّينَ وَلَوْ كَرِهَ الْكَافِرُونَ.",
                countTarget = 1,
                benefit = "إخلاص التوحيد والثناء الحسن على الله تعالى",
                sourceCitation = "صحيح مسلم (594)"
            ),
            DhikrItem(
                id = "post_tasbih",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "سُبْحَانَ اللَّهِ",
                countTarget = 33,
                benefit = "التسبيح ثلاثاً وثلاثين دبر كل صلاة",
                sourceCitation = "صحيح مسلم (597)"
            ),
            DhikrItem(
                id = "post_tahmid",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "الْحَمْدُ لِلَّهِ",
                countTarget = 33,
                benefit = "التحميد ثلاثاً وثلاثين دبر كل صلاة",
                sourceCitation = "صحيح مسلم (597)"
            ),
            DhikrItem(
                id = "post_takbir",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "اللَّهُ أَكْبَرُ",
                countTarget = 33,
                benefit = "التكبير ثلاثاً وثلاثين دبر كل صلاة",
                sourceCitation = "صحيح مسلم (597)"
            ),
            DhikrItem(
                id = "post_tamam",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ.",
                countTarget = 1,
                benefit = "إتمام المائة: غُفرت خطاياه وإن كانت مثل زبد البحر",
                sourceCitation = "صحيح مسلم (597)"
            ),
            ayatAlKursiItem,
            DhikrItem(
                id = "post_muawwidhat",
                category = DhikrCategory.POST_PRAYER,
                textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ۞ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۞ قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                countTarget = 1,
                benefit = "قراءة المعوذات دبر كل صلاة (وثلاثاً بعد الفجر والمغرب)",
                sourceCitation = "سنن أبي داود (1523) وصححه الألباني"
            )
        )
    }

    fun getMorningDhikrs(): List<DhikrItem> {
        return listOf(
            DhikrItem(
                id = "m_1",
                category = DhikrCategory.MORNING,
                textArabic = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذَا الْيَوْمِ وَخَيْرَ مَا بَعْدَهُ، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذَا الْيَوْمِ وَشَرِّ مَا بَعْدَهُ، رَبِّ أَعُوذُ بِكَ مِنَ الْكَسَلِ وَسُوءِ الْكِبَرِ، رَبِّ أَعُوذُ بِكَ مِنْ عَذَابٍ فِي النَّارِ وَعَذَابٍ فِي الْقَبْرِ.",
                countTarget = 1,
                benefit = "بداية اليوم بالاعتراف بملك الله والتعوذ من الشرور والكسل",
                sourceCitation = "صحيح مسلم (2723)"
            ),
            DhikrItem(
                id = "m_2",
                category = DhikrCategory.MORNING,
                textArabic = "اللَّهُمَّ بِكَ أَصْبَحْنَا، وَبِكَ أَمْسَيْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ النُّشُورُ.",
                countTarget = 1,
                benefit = "التوكل الكامل على الله في الصباح والمساء والحياة والموت",
                sourceCitation = "سنن الترمذي (3391) وصححه الألباني"
            ),
            DhikrItem(
                id = "m_sayyid",
                category = DhikrCategory.MORNING,
                textArabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ.",
                countTarget = 1,
                benefit = "سيد الاستغفار: من قاله موقناً به حين يصبح فمات دخل الجنة",
                sourceCitation = "صحيح البخاري (6306)"
            ),
            DhikrItem(
                id = "m_raditu",
                category = DhikrCategory.MORNING,
                textArabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا.",
                countTarget = 3,
                benefit = "من قالها ثلاثاً حين يصبح وحين يمسي كان حقاً على الله أن يرضيه",
                sourceCitation = "سنن أبي داود (5072) والترمذي (3389)"
            ),
            DhikrItem(
                id = "m_hasbi",
                category = DhikrCategory.MORNING,
                textArabic = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ.",
                countTarget = 7,
                benefit = "من قالها سبع مرات كفاه الله ما أهمه من أمر دنياه وآخرته",
                sourceCitation = "سنن أبي داود (5081)"
            ),
            DhikrItem(
                id = "m_afwa",
                category = DhikrCategory.MORNING,
                textArabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي الدُّنْيَا وَالْآخِرَةِ، اللَّهُمَّ إِنِّي أَسْأَلُكَ الْعَفْوَ وَالْعَافِيَةَ فِي دِينِي وَدُنْيَايَ وَأَهْلِي وَمَالِي، اللَّهُمَّ اسْتُرْ عَوْرَاتِي، وَآمِنْ رَوْعَاتِي، اللَّهُمَّ احْفَظْنِي مِنْ بَيْنِ يَدَيَّ، وَمِنْ خَلْفِي، وَعَنْ يَمِينِي، وَعَنْ شِمَالِي، وَمِنْ فَوْقِي، وَأَعُوذُ بِعَظَمَتِكَ أَنْ أُغْتَالَ مِنْ تَحْتِي.",
                countTarget = 1,
                benefit = "حفظ تام من جميع الجهات ودعاء العافية الشاملة",
                sourceCitation = "سنن أبي داود (5074) وصححه الألباني"
            ),
            DhikrItem(
                id = "m_ilman",
                category = DhikrCategory.MORNING,
                textArabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلًا مُتَقَبَّلًا.",
                countTarget = 1,
                benefit = "طلب ثلاثية التوفيق والبركة في بداية النهار",
                sourceCitation = "سنن ابن ماجه (925) وصححه الألباني"
            ),
            DhikrItem(
                id = "m_bismillah",
                category = DhikrCategory.MORNING,
                textArabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
                countTarget = 3,
                benefit = "من قالها ثلاثاً لم يضره شيء حتى يمسي",
                sourceCitation = "سنن الترمذي (3388) وأبو داود (5088)"
            ),
            DhikrItem(
                id = "m_subhan_adada",
                category = DhikrCategory.MORNING,
                textArabic = "سُبْحَانَ اللَّهِ وَبِحَمْدِهِ: عَدَدَ خَلْقِهِ، وَرِضَا نَفْسِهِ، وَزِنَةَ عَرْشِهِ، وَمِدَادَ كَلِمَاتِهِ.",
                countTarget = 3,
                benefit = "تعدل ساعات طويلة من الذكر والتسبيح",
                sourceCitation = "صحيح مسلم (2726)"
            ),
            DhikrItem(
                id = "m_muawwidhat_3",
                category = DhikrCategory.MORNING,
                textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ۞ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۞ قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                countTarget = 3,
                benefit = "تكفيك من كل شيء",
                sourceCitation = "سنن أبي داود (5082) والترمذي (3575)"
            )
        )
    }

    fun getEveningDhikrs(): List<DhikrItem> {
        return listOf(
            DhikrItem(
                id = "e_1",
                category = DhikrCategory.EVENING,
                textArabic = "أَمْسَيْنَا وَأَمْسَى الْمُلْكُ لِلَّهِ، وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ، رَبِّ أَسْأَلُكَ خَيْرَ مَا فِي هَذِهِ اللَّيْلَةِ وَخَيْرَ مَا بَعْدَهَا، وَأَعُوذُ بِكَ مِنْ شَرِّ مَا فِي هَذِهِ اللَّيْلَةِ وَشَرِّ مَا بَعْدَهَا...",
                countTarget = 1,
                benefit = "استقبال الليل بحمد الله وسؤال خيره واستعاذة من شره",
                sourceCitation = "صحيح مسلم (2723)"
            ),
            DhikrItem(
                id = "e_2",
                category = DhikrCategory.EVENING,
                textArabic = "اللَّهُمَّ بِكَ أَمْسَيْنَا، وَبِكَ أَصْبَحْنَا، وَبِكَ نَحْيَا، وَبِكَ نَمُوتُ، وَإِلَيْكَ الْمَصِيرُ.",
                countTarget = 1,
                benefit = "التسليم والتوكل على الله عند المساء",
                sourceCitation = "سنن الترمذي (3391)"
            ),
            DhikrItem(
                id = "e_sayyid",
                category = DhikrCategory.EVENING,
                textArabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ.",
                countTarget = 1,
                benefit = "سيد الاستغفار: من قاله موقناً به حين يمسي فمات دخل الجنة",
                sourceCitation = "صحيح البخاري (6306)"
            ),
            DhikrItem(
                id = "e_audhu",
                category = DhikrCategory.EVENING,
                textArabic = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ شَرِّ مَا خَلَقَ.",
                countTarget = 3,
                benefit = "من قالها ثلاثاً لم يضره في تلك الليلة شيء",
                sourceCitation = "صحيح مسلم (2709)"
            ),
            DhikrItem(
                id = "e_raditu",
                category = DhikrCategory.EVENING,
                textArabic = "رَضِيتُ بِاللَّهِ رَبًّا، وَبِالإِسْلَامِ دِينًا، وَبِمُحَمَّدٍ ﷺ نَبِيًّا.",
                countTarget = 3,
                benefit = "كان حقاً على الله أن يرضيه يوم القيامة",
                sourceCitation = "سنن الترمذي (3389)"
            ),
            DhikrItem(
                id = "e_hasbi",
                category = DhikrCategory.EVENING,
                textArabic = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ.",
                countTarget = 7,
                benefit = "كفاه الله ما أهمه",
                sourceCitation = "سنن أبي داود (5081)"
            ),
            DhikrItem(
                id = "e_bismillah",
                category = DhikrCategory.EVENING,
                textArabic = "بِسْمِ اللَّهِ الَّذِي لَا يَضُرُّ مَعَ اسْمِهِ شَيْءٌ فِي الْأَرْضِ وَلَا فِي السَّمَاءِ وَهُوَ السَّمِيعُ الْعَلِيمُ.",
                countTarget = 3,
                benefit = "حماية تامة من كل سوء حتى يصبح",
                sourceCitation = "سنن الترمذي (3388)"
            ),
            DhikrItem(
                id = "e_muawwidhat_3",
                category = DhikrCategory.EVENING,
                textArabic = "قُلْ هُوَ اللَّهُ أَحَدٌ ۞ قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۞ قُلْ أَعُوذُ بِرَبِّ النَّاسِ",
                countTarget = 3,
                benefit = "تكفيك من كل شيء",
                sourceCitation = "سنن الترمذي (3575)"
            )
        )
    }

    fun getSleepDhikrs(): List<DhikrItem> {
        return listOf(
            ayatAlKursiItem.copy(id = "sleep_kursi", category = DhikrCategory.SLEEP, benefit = "لا يزال عليك من الله حافظ ولا يقربك شيطان حتى تصبح"),
            DhikrItem(
                id = "sleep_baqarah_last2",
                category = DhikrCategory.SLEEP,
                textArabic = "آمَنَ الرَّسُولُ بِمَا أُنْزِلَ إِلَيْهِ مِنْ رَبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِنْ رُسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ ۞ لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِنْ نَسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِنْ قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنْتَ مَوْلَانَا فَانْصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ.",
                countTarget = 1,
                benefit = "الآيتان من آخر سورة البقرة: من قرأهما في ليلة كفتاه",
                sourceCitation = "صحيح البخاري (5009) وصحيح مسلم (808)"
            ),
            DhikrItem(
                id = "sleep_muawwidhat",
                category = DhikrCategory.SLEEP,
                textArabic = "سورة الإخلاص ۞ سورة الفلق ۞ سورة الناس (ينفث في كفيه ويمسح بهما ما استطاع من جسده)",
                countTarget = 3,
                benefit = "سنة المصطفى ﷺ عند النوم للتحصين والسكينة",
                sourceCitation = "صحيح البخاري (5017)"
            ),
            DhikrItem(
                id = "sleep_bismika",
                category = DhikrCategory.SLEEP,
                textArabic = "بِاسْمِكَ رَبِّي وَضَعْتُ جَنْبِي، وَبِكَ أَرْفَعُهُ، فَإِنْ أَمْسَكْتَ نَفْسِي فَارْحَمْهَا، وَإِنْ أَرْسَلْتَهَا فَاحْفَظْهَا بِمَا تَحْفَظُ بِهِ عِبَادَكَ الصَّالِحِينَ.",
                countTarget = 1,
                benefit = "تسليم الروح لله في المنام",
                sourceCitation = "صحيح البخاري (6320) وصحيح مسلم (2714)"
            ),
            DhikrItem(
                id = "sleep_fatima_tasbih",
                category = DhikrCategory.SLEEP,
                textArabic = "سُبْحَانَ اللَّهِ",
                countTarget = 33,
                benefit = "تسبيح فاطمة عند النوم: خير من خادم وقوة للجسد",
                sourceCitation = "صحيح البخاري (3705) وصحيح مسلم (2727)"
            ),
            DhikrItem(
                id = "sleep_fatima_tahmid",
                category = DhikrCategory.SLEEP,
                textArabic = "الْحَمْدُ لِلَّهِ",
                countTarget = 33,
                benefit = "تحميد الله 33 عند النوم",
                sourceCitation = "صحيح مسلم (2727)"
            ),
            DhikrItem(
                id = "sleep_fatima_takbir",
                category = DhikrCategory.SLEEP,
                textArabic = "اللَّهُ أَكْبَرُ",
                countTarget = 34,
                benefit = "تكبير الله 34 لإتمام المائة قبل النوم",
                sourceCitation = "صحيح مسلم (2727)"
            )
        )
    }

    fun getAllDuas(): List<DuaItem> {
        return listOf(
            // العامة
            DuaItem("dua_1", DuaCategory.GENERAL, "دعاء الدنيا والآخرة", "رَبَّنَا آتِنَا فِي الدُّنْيَا حَسَنَةً وَفِي الْآخِرَةِ حَسَنَةً وَقِنَا عَذَابَ النَّارِ.", "Our Lord, give us in this world that which is good and in the Hereafter that which is good and save us from the torment of the Fire.", "سورة البقرة: 201 (أكثر دعاء النبي ﷺ - صحيح البخاري)", isQuranic = true),
            DuaItem("dua_2", DuaCategory.GENERAL, "دعاء يونس عليه السلام", "لَا إِلَٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ.", "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.", "سورة الأنبياء: 87 (دعوة ذي النون ما دعا بها مسلم في كرب إلا استجاب الله له)", isQuranic = true),
            DuaItem("dua_3", DuaCategory.GENERAL, "حسبنا الله ونعم الوكيل", "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ.", "Sufficient for us is Allah, and [He is] the best Disposer of affairs.", "سورة آل عمران: 173 (صحيح البخاري)", isQuranic = true),
            
            // للوالدين
            DuaItem("dua_parents_1", DuaCategory.PARENTS, "دعاء الرحمة للوالدين", "رَبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا.", "My Lord, have mercy upon them as they brought me up [when I was] small.", "سورة الإسراء: 24", isQuranic = true),
            DuaItem("dua_parents_2", DuaCategory.PARENTS, "دعاء المغفرة للوالدين والمؤمنين", "رَبِّ اغْفِرْ لِي وَلِوَالِدَيَّ وَلِلْمُؤْمِنِينَ يَوْمَ يَقُومُ الْحِسَابُ.", "Our Lord, forgive me and my parents and the believers the Day the account is established.", "سورة إبراهيم: 41", isQuranic = true),

            // العلم
            DuaItem("dua_knowledge_1", DuaCategory.KNOWLEDGE, "طلب زيادة العلم", "رَبِّ زِدْنِي عِلْمًا.", "My Lord, increase me in knowledge.", "سورة طه: 114", isQuranic = true),
            DuaItem("dua_knowledge_2", DuaCategory.KNOWLEDGE, "دعاء العلم النافع", "اللَّهُمَّ انْفَعْنِي بِمَا عَلَّمْتَنِي، وَعَلِّمْنِي مَا يَنْفَعُنِي، وَزِدْنِي عِلْمًا.", "O Allah, benefit me with what You have taught me and teach me what benefits me.", "سنن الترمذي (3599) وصححه الألباني"),

            // الرزق
            DuaItem("dua_prov_1", DuaCategory.PROVISION, "دعاء موسى عليه السلام للخير والرزق", "رَبِّ إِنِّي لِمَا أَنْزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ.", "My Lord, indeed I am, for whatever good You would send down to me, in need.", "سورة القصص: 24", isQuranic = true),
            DuaItem("dua_prov_2", DuaCategory.PROVISION, "سؤال الرزق الحلال والكفاية", "اللَّهُمَّ اكْفِنِي بِحَلَالِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ.", "O Allah, suffice me with what is lawful against what is unlawful, and enrich me with Your grace.", "سنن الترمذي (3563) وحسنه"),

            // الحفظ
            DuaItem("dua_prot_1", DuaCategory.PROTECTION, "دعاء الثبات على الدين", "رَبَّنَا لَا تُزِغْ قُلُوبَنَا بَعْدَ إِذْ هَدَيْتَنَا وَهَبْ لَنَا مِنْ لَدُنْكَ رَحْمَةً ۚ إِنَّكَ أَنْتَ الْوَهَّابُ.", "Our Lord, let not our hearts deviate after You have guided us and grant us from Yourself mercy.", "سورة آل عمران: 8", isQuranic = true),
            DuaItem("dua_prot_2", DuaCategory.PROTECTION, "دعاء يامقلب القلوب", "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ.", "O Turner of the hearts, make my heart firm upon Your religion.", "سنن الترمذي (2140) وصححه"),

            // الكرب
            DuaItem("dua_relief_1", DuaCategory.RELIEF, "دعاء تفريج الهم والحزن", "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ.", "O Allah, I seek refuge in You from grief and sadness, weakness and laziness, miserliness and cowardice.", "صحيح البخاري (2893)"),
            DuaItem("dua_relief_2", DuaCategory.RELIEF, "دعاء الكرب العظيم", "لَا إِلَهَ إِلَّا اللَّهُ الْعَظِيمُ الْحَلِيمُ، لَا إِلَهَ إِلَّا اللَّهُ رَبُّ الْعَرْشِ الْعَظِيمِ، لَا إِلَهَ إِلَّا اللَّهُ رَبُّ السَّمَاوَاتِ وَرَبُّ الْأَرْضِ وَرَبُّ الْعَرْشِ الْكَرِيمِ.", "There is no deity but Allah, the Great, the Forbearing...", "صحيح البخاري (6346) وصحيح مسلم (2730)"),

            // السفر
            DuaItem("dua_travel_1", DuaCategory.TRAVEL, "دعاء ركوب الدابة والسفر", "سُبْحَانَ الَّذِي سَخَّرَ لَنَا هَٰذَا وَمَا كُنَّا لَهُ مُقْرِنِينَ ۞ وَإِنَّا إِلَىٰ رَبِّنَا لَمُنْقَلِبُونَ. اللَّهُمَّ إِنَّا نَسْأَلُكَ فِي سَفَرِنَا هَذَا الْبِرَّ وَالتَّقْوَى، وَمِنَ الْعَمَلِ مَا تَرْضَى.", "Glory to Him who has subjected this to us, and we could not have done it by ourselves...", "سورة الزخرف: 13-14 وصحيح مسلم (1342)", isQuranic = true),

            // الطعام
            DuaItem("dua_food_1", DuaCategory.FOOD, "الدعاء بعد الفراغ من الطعام", "الْحَمْدُ لِلَّهِ الَّذِي أَطْعَمَنِي هَذَا الطَّعَامَ وَرَزَقَنِيهِ مِنْ غَيْرِ حَوْلٍ مِنِّي وَلَا قُوَّةٍ.", "Praise be to Allah Who has fed me this and provided it for me without any might or power from myself.", "سنن أبي داود (4023) والترمذي (3458)"),

            // المسجد
            DuaItem("dua_mosque_1", DuaCategory.MOSQUE, "دعاء دخول المسجد", "اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ.", "O Allah, open for me the doors of Your mercy.", "صحيح مسلم (713)"),
            DuaItem("dua_mosque_2", DuaCategory.MOSQUE, "دعاء الخروج من المسجد", "اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنْ فَضْلِكَ.", "O Allah, I ask You from Your bounty.", "صحيح مسلم (713)"),

            // المطر والاستسقاء
            DuaItem("dua_rain_1", DuaCategory.RAIN, "دعاء نزول المطر", "اللَّهُمَّ صَيِّبًا نَافِعًا.", "O Allah, make it a beneficial downpour.", "صحيح البخاري (1032)"),
            DuaItem("dua_istisqa_1", DuaCategory.ISTISQA, "دعاء الاستسقاء", "اللَّهُمَّ اسْقِنَا غَيْثًا مُغِيثًا مَرِيئًا مَرِيعًا نَافِعًا غَيْرَ ضَارٍّ، عَاجِلًا غَيْرَ آجِلٍ.", "O Allah, give us rain that brings relief, wholesome, productive, beneficial not harmful.", "سنن أبي داود (1169) وصححه الألباني"),

            // الزواج والأبناء
            DuaItem("dua_child_1", DuaCategory.CHILDREN, "دعاء هبة الذرية الصالحة", "رَبِّ هَبْ لِي مِنْ لَدُنْكَ ذُرِّيَّةً طَيِّبَةً ۖ إِنَّكَ سَمِيعُ الدُّعَاءِ.", "My Lord, grant me from Yourself a good offspring. Indeed, You are the Hearer of prayer.", "سورة آل عمران: 38", isQuranic = true),
            DuaItem("dua_marriage_1", DuaCategory.MARRIAGE, "دعاء قرة الأعين والتقوى", "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا.", "Our Lord, grant us from among our wives and offspring comfort to our eyes...", "سورة الفرقان: 74", isQuranic = true),

            // المغفرة والجنة والنار
            DuaItem("dua_forgiv_1", DuaCategory.FORGIVENESS, "دعاء آدم وحواء عليهما السلام", "رَبَّنَا ظَلَمْنَا أَنْفُسَنَا وَإِنْ لَمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ.", "Our Lord, we have wronged ourselves, and if You do not forgive us and have mercy upon us, we will surely be among the losers.", "سورة الأعراف: 23", isQuranic = true),
            DuaItem("dua_jannah_1", DuaCategory.PARADISE, "سؤال الجنة والاستعاذة من النار", "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْجَنَّةَ وَمَا قَرَّبَ إِلَيْهَا مِنْ قَوْلٍ أَوْ عَمَلٍ، وَأَعُوذُ بِكَ مِنَ النَّارِ وَمَا قَرَّبَ إِلَيْهَا مِنْ قَوْلٍ أَوْ عَمَلٍ.", "O Allah, I ask You for Paradise and whatever word or action brings one near to it, and I seek refuge from Hellfire...", "مسند أحمد وسنن ابن ماجه (3846) وصححه الألباني")
        )
    }

    val standardTasbihList = listOf(
        TasbihItem("tasbih_subhan", "سُبْحَانَ اللَّهِ", "Subhan Allah", 33, 0, 0, "أحب الكلام إلى الله، وتغرس لك بها نخلة في الجنة"),
        TasbihItem("tasbih_hamd", "الْحَمْدُ لِلَّهِ", "Alhamdulillah", 33, 0, 0, "تملأ الميزان بالخيرات والحسنات"),
        TasbihItem("tasbih_akbar", "اللَّهُ أَكْبَرُ", "Allahu Akbar", 33, 0, 0, "تعظيم لله ورفع للدرجات"),
        TasbihItem("tasbih_astaghfir", "أَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ", "Astaghfirullah", 100, 0, 0, "ممحاة للذنوب ومجلبة للرزق والبركة"),
        TasbihItem("tasbih_salawat", "اللَّهُمَّ صَلِّ وَسَلِّمْ عَلَى نَبِيِّنَا مُحَمَّدٍ", "Allahumma Salli Ala Muhammad", 100, 0, 0, "من صلى عليّ صلاة صلى الله عليه بها عشراً"),
        TasbihItem("tasbih_tahlil", "لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ", "La Ilaha Illa Allah", 100, 0, 0, "أفضل ما قلته أنا والنبيون من قبلي"),
        TasbihItem("tasbih_hawqala", "لَا حَوْلَ وَلَا قُوَّةَ إِلَّا بِاللَّهِ", "La Hawla Wala Quwwata Illa Billah", 33, 0, 0, "كنز من كنوز الجنة")
    )
}
