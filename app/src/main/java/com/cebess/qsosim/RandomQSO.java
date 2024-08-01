package com.cebess.qsosim;

import android.util.Log;

import java.io.IOException;
import java.text.ParseException;

/**
 * RandomQSO defines a grammar that generates random amateur radio QSO's.
 * It is based on a similar grammar published by Paul J. Drongowski (NEOQT)
 * in his SuperiorMorse program. As well as a program by Martin Minow
 * Copyright &copy; 2016
 *      <a href="mailto:chas.bess@gmail.com">Charles Bess</a>.
 * </p>
 * <p>
 * </p>
 * @version 0.1
 */
public class RandomQSO extends RandomSentence
{
    private static final String noviceLicense   = "Novice";
    private static final String techLicense     = "Technician";
    private static final String generalLicense  = "General";
    private static final String advancedLicense = "Advanced";
    private static final String extraLicense    = "Extra";
    private static final String[] licenseClasses = {
            noviceLicense,
            techLicense,
            generalLicense,
            advancedLicense,
            extraLicense
    };
    private static final String[] callSignFormat = {
            "<Call2x3>",
            "<Call2x3>",
            "<Call1x3>",
            "<Call1x3>",
            "<Call2x2>",
            "<ExtraCallSign>"
    };

    private static final String grammar =
            "<Transceiver> Collins <Collins> | Drake <Drake> | Elecraft <Elecraft>"
                    + " | Hallicrafters <Hallicrafters> | Heathkit <Heathkit> | Icom <Icom>"
                    + " | Kenwood <Kenwood> | Alinco <Alinco> "
                    + " | Swan <Swan> | Ten Tec <TenTec> | Yaesu <Yaesu> | Flex <FlexRadio> | Homebrew\n"
                    + "<Elecraft> K3 | KX2 | K3S with P3 Panadapter | KX3 | K2 built from kit\n"
                    + "<Collins> 718U-5M | KWM-2\n"
                    + "<Drake> TR-3 | TR-4 | TR-4C | TR-5| TR-7\n"
                    + "<Hallicrafters> SR150 | SR160 | SR400 | SR500 | SR2000\n"
                    + "<Heathkit> DX60 w/Knight R-100A | HW7 | HW16 with HG-10 VFO | HW16 | HW32 | HW12A | HW22A | HW101 | SB201 | SB401 & SB303\n"
                    + "<Icom> IC575 | IC7300 | IC726 | IC730 | IC745 | IC756 | IC7100 | IC7300 | IC7600 | IC7610 | IC7700 | IC7300\n"
                    + "<Kenwood> TS-480SAT | TS-480HX| TS-590SG | TS-990S | TS-2000 | TR450 | TR751 | TR850 | TR851 | TS990S | TS890S | TS590SG\n"
                    + "<Swan> 120 | 140 | 180 | 240 | 350\n"
                    + "<TenTec> 585 | 562 | Delta | Argonaut | Omni\n"
                    + "<Yaesu> FT891 | FT450D | FT991A | FT757 | FT767 | FTDX1200 | FTDX3000 | FTDX10 | FTDX101MP\n"
                    + "<Alinco> DX-SRST | DX-SR8T\n"
                    + "<FlexRadio> 6400 | 6600M\n"
                    + "<Antenna> beam | delta loop | dipole | double zepp | half wave dipole"
                    + " | inverted V | whip | delta loop | parasitic beam | log periodic"
                    + " | quad loop | quad vertical | quagi | rhombic | longwire | mobile screwdriver"
                    + " | trap doublet | yagi | zepp | monobander | tribander | symmetrical delta loop"
                    + " | 3 element beam | 5 band vertical | 5 element loop | single band half wave dipole"
                    + " | longwire with an autotuner | cubical quad | vertical with 10 radials | dipole in the attic"
                    + " | whip with an autotuner | 7 band beam | G5RV | Buddipole | Rombic | beverage | long wire with tuner\n"
                    + "<UpFeet> <OneToTen><ZeroOrFive>\n"
                    + "<OneToTen> 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | 10\n"
                    + "<ZeroOrFive> 0 | 5\n"
                    + "<Weather> sunny | rain | freezing rain | sleet | snow"
                    + " | cloudy | partly cloudy | partly sunny | clear"
                    + " | cold <and> windy | raining | snowing | sunny | hot"
                    + " | windy <and> warm | cloudy | drizzling | foggy | wet/foggy"
                    + " | fog/drizzle | hot/muggy | hot/dry | cool/windy | smoggy"
                    + " | hot/smoggy | cold/dry | hot/humid | warm | windy"
                    + " | <Very> hot | <Very> cold | <Very> windy | wet/windy | humid | hot <and> humid\n"
                    + "<Power> QRP 5 | QRP 2 | 10 | 20 | 25 | 40 | 50 | 80 | 100 | 125 | 140"
                    + " | 150 | 170 | 200 | 250 | 270 | 300\n"
                    + "<Job> engineer | nurse | fireman | mechanic | programmer | carpenter"
                    + " | electrician | writer | teacher | doctor | attorney | lawyer | psychiatrist"
                    + " | clerk | chemist | librarian | teller | web designer | physicist | builder"
                    + " | mathematician | professor | driver | milkman | gardener | data analyst"
                    + " | bricklayer | guard | dentist | curator | farmer | stock broker"
                    + " | letter carrier | designer | student | college student | kindergarden teacher"
                    + " | high school teacher | administrator | police officer | systems engineer"
                    + " | investment banker | politician | xray technician\n"
                    + "<Name> Al | Alan | Alice | Allen | Alex | Alexeev | Amber | Anne"
                    + " | Art | Barbara | Bart | Betty | Bea | Bill | Bob | Bruce | Bud | Blaine | Bree | Buddy"
                    + " | Carl | Carol | Cathy | Charlie | Cheryl | Chris | Christy | Chuck | Dale"
                    + " | Dave | David | Dennis | Diane | Dick | Dan | Don | Ed | Elaine"
                    + " | Ellen | Francie | Fred | Gary | Helen | Ingrid | Frank | George"
                    + " | Gilda | Gus | Harry | Henry | Jack | James | Jane | Janet"
                    + " | Jeff | Jessica | Jill | Jim | Joan | Joe | John | Jon | Kathy"
                    + " | Kevin | Karen | Karl | Keith | Ken | Kent | Kristen | Kurt"
                    + " | Larry | Lauren | Linda | Lou | Lynda | Lynn"
                    + " | Mark | Margaret | Marv | Maria | Mark | Martin | Marty | Mary | Michelle"
                    + " | Mike | Monica | Nancy | Neil | Noelani | Oliver | Olivia | Pat"
                    + " | Patrick | Paul | Paula | Peter | Phil | Ralph | Ray | Rex"
                    + " | Rich | Rick | Roy | Ron | Sally | Sam | Scott | Scottie"
                    + " | Spencer | Steve | Stu | Sue | Terry | Tim | Todd | Tony"
                    + " | Thomas | Walt | Wendy | William | Zelda\n"
                    + "<AnyCallSign> <Call2x3> | [5] <Call1x3> | <Call2x2> | <Call1x2> | <Call2x1>"
                    + " | G<Digits><Letters><Letters><Letters>"
                    + " | VE<Digits><Letters><Letters><Letters>\n"
                    + "<ExtraCallSign> <Call1x2> | <Call2x1>\n"
                    +" <Period> [2]. | \n"
                    + "<Call2x3> <AKWN><Letters><Digits><Letters><Letters><Letters>\n"
                    + "<Call1x3> <AKWN><Digits><Letters><Letters><Letters>\n"
                    + "<Call2x2> <AKWN><Letters><Digits><Letters><Letters>\n"
                    + "<Call1x2> <AKWN><Digits><Letters><Letters>\n"
                    + "<Call2x1> <AKWN><Letters><Digits><Letters>\n"
                    + "<AKWN> A | K | W | N \n"
                    + "<Digits> 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9\n"
                    + "<Letters> A | B | C | D | E | F | G | H | I | J | K | L"
                    + " | M | N | O | P | Q | R | S | T | U | V | W | X | Y | Z\n"
                    + "<LicenseClass>"  /* These strings will be used to generate the license class */
                    +   noviceLicense
                    + " | [2] "     + techLicense
                    + " | [4] "     + generalLicense
                    + " | "         + advancedLicense
                    + " | [3]"         + extraLicense
                    + "\n"
                    + "<City> Aiea | Alexander | Asbury | Baker | Beckley | Bedford"
                    + " | Berkeley | Brunswick | Brunsville | Chicago | Cambridge"
                    + " | Charleston | Circleville | Clarksburg | Clear Lake | Cleveland"
                    + " | Crystal | Duncanville | Elizabeth | Ewali | Fairbanks | Fairfield"
                    + " | Flint | Gahanna | Grant | Greensburg | Harper | Hargen "
                    + " | Hillsdale | Jamestown | Jefferson | Kaaawa | Konark"
                    + " | Lawrenceville | Lakewood | Lincoln | Litchfield | London"
                    + " | Long Beach | Lyndhurst | Lynnville | Mansfield | Maple | Medows"
                    + " | Mentor | Mercer | Mewquite | Midland | Milldale | Milltown"
                    + " | Moorestown | Mountain View | Murray | Newfield | Newport"
                    + " | New London | Olmstead | Oak | Okatie | Oxnard | Oxford | Paradise"
                    + " | Paris | Perry | Peru | Potter | Pottsville | Redwood | Russellville"
                    + " | Salem | Sandy | Saratoga | Smithville | Springdale | Springfield"
                    + " | Starkville | Sunnyvale | Tinker | Trenton | Walnut Creek | Wabash "
                    + " | Warsaw | Washington | Weston | Wheatfield | Williamson"
                    + " | Worchester | Zion"
                    + " | [23] <New> <NewCity> | New York City"
                    + " | [40] <CityHeights> <Heights>\n"
                    + "<NewCity> Albany | Avalon | Barnard | Brunswick | Bedford"
                    + " | Chester | Conway | Box | Franklin | Granville"
                    + " | Hamilton | London | Morris | Oxford | Salisbury | Stafford"
                    + " | Stanton | Trenton | Troy | Walpole | Warren | Weston"
                    + " | Windsor\n"
                    + "<CityHeights> Apple | Ashford | Baker | Baldwin | Banner"
                    + " | Barnard | Benton | Carson | Chester | Conway "
                    + " | Cornwall | Crystal | Fletcher | Franklin | Granite"
                    + " | Crant | Harper | Jefferson | Hamilton | Hickory"
                    + " | Lincoln | Maple | Mercer | Morgan | Morris | Murray"
                    + " | Oak | Orwell | Quail | Perry | Potter | Salem | Stafford"
                    + " | Stone | Tinker | Walnut | Warren | Washington | Weston "
                    + " | Wilton\n"
                    + "<New> New | Old | North | South | East | West\n"
                    + "<Heights> Castle | Heights | Island | Valley | City | Creek | Park | Mill | Neck\n"
                    + "<Good> [3] good | gud \n"
                    + "<Quality> outstanding | <Very> good | <Good> | OK | Poor | <Very> poor\n"
                    + "<QRT> [4] QRT | leave | take a break | get going\n"
                    + "<MustQRT> Lots of QRM "
                    + " | Lots of QRM. I cant hear you"
                    + " | Lots of QRM. cant copy"
                    + " | <QRT> for dinner "
                    + " | Must <QRT> for dinner "
                    + " | Must <QRT> for a nap "
                    + " | Must <QRT> for tea"
                    + " | Must <QRT>, wife has something for me to do."
                    + " | Must <QRT> for lunch."
                    + " | Must <QRT> for a snack"
                    + " | Must <QRT> for sleep."
                    + " | Must <QRT> and take a break"
                    + " | Must <QRT> for bathroom break "
                    + " | Must <QRT> lightning threatens "
                    + " | Must <QRT> lightning storm has started "
                    + " | Must <QRT> tornado sirens sounding "
                    + " | Must <QRT> tsunami sirens sounding "
                    + " | Must <QRT> to hear news about approaching hurricane"
                    + " | Must <QRT> to check email."
                    + " | Must <QRT> weather radio alarm just went off."
                    + " | [5] Must <QRT> for sked with <Someone>."
                    + " | Must <QRT> <Someone> is at the front door."
                    + " | QRM I cant make <You> out of the other signals | QRM? the band is crowded"
                    + " | QRN I cant pull <You> out of the noise | QRN? the band is noisy <Here>"
                    + " | QSB the band is a bit unstable | QSB? band seems to be changing"
                    + " | QRS pse I cant copy that fast | QRS? I can slow down"
                    + " | QSY? there is QRM on this freq | QSK? in case I need to interupt <You> | QRX? so we can work again | QTH? | QTR? My clock is broken"
                    + " | What is <Your> QTH?\n"
                    + "<Someone> my uncle | my brother | my sister | my mom\n"
                    + "<States> AL | AK | AZ | AR | CA | CO | CT | DE | FL | GA | HI | ID | IL | IN | IA | KS | KY | LA | ME | MD | MA | MI | MN | MS | MO | MT | NE | NV"
                    + " | NH | NJ | NM | NY | NC | ND | OH | OK | OR | PA | RI | SC | SD | TN | TX | UT | VT | VA | WA | WV | WI | WY \n"
                    + "<StatesLong> Alabama | Alaska | Arizona | Arkansas | California"
                    + " | Colorado | Connecticut | Delaware | Florida | Gaum | Georgia"
                    + " | Hawaii | Idaho | Illinois | Indiana | Iowa | Kansas | Kentucky"
                    + " | Louisiana | Maine | Maryland | Massachusetts | Michigan | Midway"
                    + " | Minnesota | Mississippi | Missouri | Montana | Nebraska | Nevada"
                    + " | New Hampshire | New Jersey | New Mexico | New York | North Carolina"
                    + " | North Dakota | Ohio | Oklahoma | Oregon | Pennsylvania | Puerto Rico"
                    + " | Rhode Island | South Carolina | South Dakota"
                    + " | Tennessee | Texas | Utah | Vermont | Virginia | Virgin Islands | Guantanamo Bay | American Samoa "
                    + " | Wake Island | Washington | West Virginia | Wisconsin | Wyoming\n"
                    + "<Talker> bloviate | loquacious | garrulous | voluble\n"
                    + "<Miscellaneous> I sometimes work DSTAR on vhf <Period>"
                    + " | I usually work APRS on vhf<Period> Do you?"
                    + " | [2] We <Are> on vacation <and> I am mobile<Period>"
                    + " | Your sig is chirpy<Period>"
                    + " | Is it green there or brown?"
                    + " | Do you have antenna restrictions?"
                    + " | Where do you live?"
                    + " | Do you ever run mobile?"
                    + " | How long have <You> been a ham?"
                    + " | How long have <You> been a radio amateur?"
                    + " | What <Are> <Your> other hobbies?"
                    + " | What part of ham radio do you like best?"
                    + " | I am mobile <and> going to the office<Period>"
                    + " | I am mobile <and> am going to work<Period>"
                    + " | I am mobile <and> driving to work<Period>"
                    + " | I am mobile, driving home from work<Period>"
                    + " | I am mobile, stuck in traffic<Period>"
                    + " | I am mobile, driving cross country<Period>"
                    + " | I am mobile, riding a bike<Period> Thats hard to do and send CW<Period>"
                    + " | How is my signal?"
                    + " | QSL?"
                    + " | I am feeling <Quality> today. How about you?"
                    + " | How is <Your> WX?"
                    + " | How copy so far?"
                    + " | How copy?"
                    + " | Tnx for QSO <Period>"
                    + " | Do <You> ever use an iambic keyer?"
                    + " | Cpy?"
                    + " | <Thanks> for the report<Period>"
                    + " | <Thanks> for the call<Period>"
                    + " | Do <You> ever run SSB?"
                    + " | Do <You> operate PSK?"
                    + " | Do <You> operate FT8?"
                    + " | Have <You> ever foxhunted on VHF?"
                    + " | Have <You> ever been to <StatesLong>?"
                    + " | Do <You> ever use a straight key?"
                    + " | What is <Your> job?"
                    + " | Whats <Your> job?"
                    + " | How is the weather there?"
                    + " | Hows the weather?"
                    + " | How is the weather?"
                    + " | What kind of power supply <Are> <You> using?"
                    + " | Are <You> using an antenna tuner?"
                    + " | I sometimes use a linear amplifier<Period>"
                    + " | Are <You> using a linear amplifier?"
                    + " | Using a linear amplifier?"
                    + " | [5] Propagation is <Quality><Period>"
                    + " | Are <You> on daylight savings time?"
                    + " | Did <You> hear the news?"
                    + " | Did <You> hear any news?"
                    + " | Is the bad news true?"
                    + " | Sorry, just pressed the wrong macro button but I think I am <Good> now<Period>"
                    + " | There is an eclipse of the moon right now<Period>"
                    + " | Do <You> recycle often?"
                    + " | Do <You> prefer metric units? I seem to be using them more"
                    + " | We just felt a small quake <Here><Period>"
                    + " | Whoa, we <Are> having an earthquake <Here>. A real roller<Period>"
                    + " | <You> <Are> my first contact today<Period>"
                    + " | <You> <Are> my second contact today<Period>"
                    + " | <You> <Are> my last contact today<Period>"
                    + " | <Are> <You> married?"
                    + " | Do <You> have a horse?"
                    + " | Do <You> have a dog?"
                    + " | Do <You> have a cat?"
                    + " | Do <You> have a pet fish?"
                    + " | Do <You> have a snake?"
                    + " | My clumsy cat just stepped on my hand<Period>"
                    + " | How old is <Your> <Rig>?"
                    + " | How did <You> learn morse code?"
                    + " | How fast can <You> send morse code?"
                    + " | When did <You> start to learn morse code?"
                    + " | <Are> u <Quality> at calculus? I have a question for <You><Period> HIHI"
                    + " | <Are> u <Quality> at antenna design? I have a question for <You><Period>"
                    + " | Have <You> hung an antenna in a tree? That is supposed to be easy<Period>"
                    + " | Do <You> own a 3D printer?"
                    + " | How long have <You> been a ham?"
                    + " | Have <You> ever ran a net?"
                    + " | There is a rainbow outside the window."
                    + " | I just saw a bolide<Period> Have <You> ever worked meteor shower?"
                    + " | Do <You> know the word copacetic, if so thats excellent<Period>"
                    + " | <Are> <You> a ragchewer? Do <You> know the definition of <Talker>?"
                    + " | What time zone <Are> <You> in?"
                    + " | Would <You> have given Morse a Nobel prize?"
                    + " | Our neighbors have a horse named Morse<Period>"
                    + " | What is your elevation?"
                    + " | What is the barometric air pressure there?"
                    + " | I sometimes monitor ULF for earthquake precursors.Do <You>?"
                    + " | Can <You> think of something else that I should know about?"
                    + " | Anything else I should know about <You>?"
                    + "\n"
                    + "<and> and | es\n"
                    + "<Very> very | vy\n"
                    + "<RSTDigits> [25] 5<5To9><5To9> | 5nn | 354 | 248 | 354 | 599\n"
                    + "<5To9> 5 | 6 | 7 | 8 | 9\n"
                    + "<1To3> 1 | 2 | 3\n"
                    + "<0To2> 1| 2|\n"
                    + "<Here> here | hr |\n"
                    + "<Thanks> Thanks | tnx | Thank <You>\n"
    /* Here are some larger chunks */
                    + "<MyThanks> <Thanks> for <Your> call | <Thanks> for ur call | <Thanks> for the call OM | <Thanks> for the call\n"
                    + "<MyName> Name is <Name><Period> | Name <Name> | Name <Here> is <Name> | My name is <Name>.\n"
                    + "<MyJob> [3] I am a <Job><Period> | I work as a <Job><Period> | I was a <Job>, now retired<Period>"
                    + "| I was a <Job> but now retired<Period> | I was a <Job> but now looking to change jobs<Period> | I was a <Job> but changing jobs<Period>\n"
                    + "<MyAge> I am <1To8><Digits> years old<Period> | My age is <1To8><Digits> | age <1To8><Digits> | Age is <1To8><Digits>\n"
                    + "<1To8> 1 | 2 | 3 | 4 | 5 | 8 | 7 | 8\n"
                    + "<MyLicense> I have a <SenderLicense> license. | I am a <SenderLicense> class ham<Period> "
                    + " | I have had a <SenderLicense> license for <5To9> years. | I have been a <SenderLicense> for <5To9> years<Period> "
                    + " | I have had a <SenderLicense> license for <1To3><Digits> years<Period> \n"
                    + "<MyTemperature> Temp <Here> is <Temperature> | Temp is <Temperature> \n"
                    + "<MyWeather> <WeatherText> <MyTemperature><Period>"
                    + " | <MyTemperature> <WeatherText><Period> "
                    + " | Temp <Is> <Temperature> degrees <Here> <and> <WeatherText><Period> "
                    + " | <Weather> <Here> <and> <Temperature> degrees<Period> \n"
                    + "<WeatherText> <WeatherName> <Is> <Weather>"
                    + " | <WeatherName> <Here> <Is> <Weather> <Wind>"
                    + " | It <Is> <Weather> <Here> <Wind>"
                    + " | It <Is> <Weather>\n"
                    + "<Is> is | \n"
                    + "<WeatherName> [3] WX | Weather | conditions\n"
                    + "<Wind> wind is <1To3><Digits> mph from the <Direction>\n"
                    + "<Direction> North | South | East | West | NNW | SSE | NNE | SSW\n"
                    + "<Temperature> <1To3><Digits> F| <0To2><Digits> C| <1To3><Digits> \n"
                    + "<CityState> <City>, <States> | <City>, <StatesLong>\n"
                    + "<MyLocation> [4] QTH <CityState> | location is <CityState><Period>  | My QTH is <CityState><Period>  | QTH is <CityState><Period> \n"
                    + "<Rig> [4] rig | radio | setup\n"
                    + "<MyRig> My <Rig> runs <Power> watts into a <Antenna> up <UpFeet> <Feet> "
                    + " | <Rig> <Is> <Transceiver> running <Power> watts, antenna is a <Antenna> "
                    + " | <Rig> <Is> <Transceiver> running <Power> watts, ant is a <Antenna> "
                    + " | My <Rig> <Is> <Transceiver>. It runs <Power> watts into a <Antenna> My antenna is up <UpFeet> <Feet><Period> "
                    + " | <Rig>  <Here> <Is> <Transceiver> running <Power> watts into a <Antenna> up <UpFeet> <Feet><Period> \n"
                    + "<YourRST> <Your> <Signal> is <RST> <RST><Period> \n"
                    + "<Feet> feet | ft | meters\n"
                    + "<You> [2] you | U\n"
                    + "<Your> [2] your | UR | \n"
                    + "<Are> [4] are | r | \n"
                    + "<Signal> [2] RST | Signal\n"
                    + "<CallSigns> <Receiver> de <Sender>\n"
                    + "<Opt73> [2] 73 | 73 <and> tnx for QSO. | Gud DX 73 | 73 c u down the log | 73 later | <Good> DX 73 | 73 Tnx for QSO \n"
                    + "<LongQSOText> <YourRST> <MyName> <MyLocation> <Miscellaneous>"
                    +           " <MyRig> <MyWeather> <MyJob> <MyAge> <MyLicense>"
                    + " | <MyLocation> <YourRST> <MyRig> <MyWeather> <Miscellaneous>"
                    +           " <MyName> <MyLicense> <MyAge> <MyJob>"
                    + " | <MyThanks> <YourRST> <MyName> <MyWeather> <MyLocation>"
                    +           " <MyJob> <MyLicense> <MyRig> <MustQRT> <MyAge>"
                    + " | <MyLocation> <YourRST> <MyRig> <Miscellaneous> <MyName>"
                    +           " <MyAge> <MyJob> <MyLicense> <MyWeather> <MustQRT> "
                    + " | <MyThanks> <YourRST> <MyJob> <Miscellaneous> <Miscellaneous>"
                    +           " <MyName> <MyAge> <MyLicense>"
                    +           " <MyRig> <MyLocation> <MyWeather> <Miscellaneous> <MustQRT> "
                    + " | <MyLocation> <YourRST> <MyRig> <MyName> <MyJob> <MyAge>"
                    +           " <Miscellaneous> <MyLicense> <MyWeather>"
                    +           " <Miscellaneous>\n"
                    + "<MediumQSOText> <YourRST> <MyName> <MyLocation> <MyRig> "
                    + " | <MyName> <MyAge> <YourRST> <MyRig> <MyLocation> "
                    + " | <YourRST> <MyName> <MyAge> <MyLocation> <MyRig> "
                    + " | <YourRST>  <MyRig> <MyName> <MyAge> <Miscellaneous> "
                    + " | <YourRST>  <MyRig> <MyName> <MyLocation> <Miscellaneous> "
                    +   "\n"
                    + "<ShortQSOText> <YourRST> <MyName> <MyRig> "
                    + " | <MyName> <YourRST> <MyRig> "
                    + " | <MyName> <YourRST> <MyLocation>"
                    + " | <MyName> <YourRST> <MyRig> <MyLocation>"
                    + " | <YourRST> <MyName> <MyAge> <MyRig> "
                    + " | <YourRST>  <MyRig> <MyName> <MyAge> "
                    + " | <YourRST>  <MyRig> <MyName> <MyLocation> "
                    +   "\n"
      /* LongQSO is for 15 WPM or faster QSO's */
                    + "<LongQSO> <CallSigns> <LongQSOText> <Opt73> <CallSigns> kn\n"
      /* MediumQSO is for 10 WPM to 15 WPM */
                    + "<MediumQSO> <CallSigns> <MediumQSOText> <MustQRT> <Opt73>  <CallSigns> kn | <CallSigns>  <MediumQSOText> <MyWeather> <MustQRT> <Opt73>  <CallSigns> kn\n"
      /* ShortQSO is for < 10 WPM */
                    + "<ShortQSO> <CallSigns> <ShortQSOText> <Opt73> <CallSigns> k\n"
     /* RandomDigits returns a 5-character "word" consisting of digits */
                    + "<RandomDigits> <Digits><Digits><Digits><Digits><Digits>\n"
     /* Random2Element returns a 5-character "word" using the 1 and 2-element symbols */
                    + "<Random2Element> <TwoElem><TwoElem><TwoElem><TwoElem><TwoElem>\n"
                    + "<TwoElem> A | E | I | M | N | T\n"
     /* Random3Element returns a 5-character "word" using the 3-element symbols */
                    + "<Random3Element> <ThreeElem><ThreeElem><ThreeElem><ThreeElem><ThreeElem>\n"
                    + "<ThreeElem> D | G | K | O | R | S | U | W\n"
     /* Random4Element returns a 5-character "word" using the 3-element symbols */
                    + "<Random4Element> <FourElem><FourElem><FourElem><FourElem><FourElem>\n"
                    + "<FourElem> B | C | F | H | J | L | P | Q | V | X | Y | Z\n"
     /* RandomPunct returns a 5-character "word" using the punctuation and prosigns */
                    + "<RandomPunct> <RandomPunct><RandomPunct><RandomPunct><RandomPunct><RandomPunct>\n"
                    + "<RandomPunct> . | , | / | ? | = | + | #\n"
    /* These will be replaced for each QSO */
                    + "<SenderLicense> <LicenseClass>\n"
                    + "<Sender> <AnyCallSign>\n"
                    + "<Receiver> <AnyCallSign>\n"
            ;

    /**
     * Create a grammar that can generate random QSO's.
     */
    public RandomQSO()
            throws ParseException, IOException
    {
        super(grammar);
        if (MainActivity.DEBUG && checkAllSymbols() > 0) {
            writeRules();
        }
    }

    /**
     * Return a random QSO as a string.
     * @return the QSO as a string.
     */
    public String getQSO(int wpm)
    {
        /*
         * Populate the sender, receiver, and RST
         */
        String senderLicense    = expand("<LicenseClass>");
        String callSign         = callSignFormat[0];
        for (int i = 0; i < licenseClasses.length; i++) {
            if (senderLicense.equals(licenseClasses[i])) {
                callSign        = callSignFormat[i];
                break;
            }
        }
        Log.d(MainActivity.ProjectName,"callSign: " + callSign);
        String sender           = expand(callSign);
        Log.d(MainActivity.ProjectName,"sender: " + sender);
        String receiver;
        do {
            receiver    = expand("<AnyCallSign>");
        } while (receiver.equals(sender));
        String RST      = expand("<RSTDigits>");
        /*
         * Store these back into the main grammar.
         */
        try {
            addRule("<SenderLicense> " + senderLicense);
            addRule("<Sender> " + sender);
            addRule("<Receiver> " + receiver);
            addRule("<RST> " + RST);
        }
        catch (Exception e) {
            Log.e(MainActivity.ProjectName,"Can't compile QSO: " + e);
        }
        String result;
        if (wpm < 10) {
            result              = expand("<ShortQSO>");
        }
        else if (wpm < 15) {
            result              = expand("<MediumQSO>");
        }
        else {
            result              = expand("<LongQSO>");
        }
        return (result);
    }
}
