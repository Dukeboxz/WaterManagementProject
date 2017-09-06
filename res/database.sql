--
-- PostgreSQL database dump
--

-- Dumped from database version 9.5.7
-- Dumped by pg_dump version 9.5.7

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: environment; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE environment (
    id integer NOT NULL,
    name character(20),
    value numeric
);


ALTER TABLE environment OWNER TO stephen;

--
-- Name: garden; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE garden (
    id integer NOT NULL,
    name character(20),
    userid integer,
    location character(30),
    location_reference character(30)
);


ALTER TABLE garden OWNER TO stephen;

--
-- Name: garden_id_seq; Type: SEQUENCE; Schema: public; Owner: stephen
--

CREATE SEQUENCE garden_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE garden_id_seq OWNER TO stephen;

--
-- Name: garden_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: stephen
--

ALTER SEQUENCE garden_id_seq OWNED BY garden.id;


--
-- Name: gardenusers; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE gardenusers (
    gardenid integer,
    userid integer,
    editrights boolean
);


ALTER TABLE gardenusers OWNER TO stephen;

--
-- Name: plants; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE plants (
    id integer NOT NULL,
    name character(30) NOT NULL,
    type integer,
    st1_days integer,
    st1_ow numeric,
    st1_bw numeric,
    st2_days integer,
    st2_ow numeric,
    st2_bw numeric,
    st3_days integer,
    st3_ow numeric,
    st3_bw numeric,
    st4_days integer,
    st4_ow numeric,
    st4_bw numeric,
    plants_persqm numeric
);


ALTER TABLE plants OWNER TO stephen;

--
-- Name: plants_id_seq; Type: SEQUENCE; Schema: public; Owner: stephen
--

CREATE SEQUENCE plants_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE plants_id_seq OWNER TO stephen;

--
-- Name: plants_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: stephen
--

ALTER SEQUENCE plants_id_seq OWNED BY plants.id;


--
-- Name: planttypes; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE planttypes (
    id integer NOT NULL,
    name character(20)
);


ALTER TABLE planttypes OWNER TO stephen;

--
-- Name: plots; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE plots (
    id integer NOT NULL,
    name character varying(30),
    size numeric NOT NULL,
    plantid integer,
    gardenid integer,
    soiltypeid integer,
    environmentid integer,
    day_planted integer,
    month_planted integer,
    year_planted integer,
    priority integer,
    CONSTRAINT plots_priority_check CHECK ((priority > 0)),
    CONSTRAINT plots_priority_check1 CHECK ((priority < 4)),
    CONSTRAINT plots_size_check CHECK ((size >= (1)::numeric))
);


ALTER TABLE plots OWNER TO stephen;

--
-- Name: plots_id_seq; Type: SEQUENCE; Schema: public; Owner: stephen
--

CREATE SEQUENCE plots_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE plots_id_seq OWNER TO stephen;

--
-- Name: plots_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: stephen
--

ALTER SEQUENCE plots_id_seq OWNED BY plots.id;


--
-- Name: soiltype; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE soiltype (
    id integer NOT NULL,
    name character(20),
    value numeric
);


ALTER TABLE soiltype OWNER TO stephen;

--
-- Name: test; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE test (
    name character(20) NOT NULL
);


ALTER TABLE test OWNER TO stephen;

--
-- Name: users; Type: TABLE; Schema: public; Owner: stephen
--

CREATE TABLE users (
    id integer NOT NULL,
    name character varying(30) NOT NULL,
    email character varying(30) NOT NULL,
    password text NOT NULL
);


ALTER TABLE users OWNER TO stephen;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: stephen
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE users_id_seq OWNER TO stephen;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: stephen
--

ALTER SEQUENCE users_id_seq OWNED BY users.id;


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY garden ALTER COLUMN id SET DEFAULT nextval('garden_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plants ALTER COLUMN id SET DEFAULT nextval('plants_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plots ALTER COLUMN id SET DEFAULT nextval('plots_id_seq'::regclass);


--
-- Name: id; Type: DEFAULT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY users ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- Data for Name: environment; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY environment (id, name, value) FROM stdin;
1	Polytunnel          	1.25
2	Raised beds         	1.05
3	Normal              	1.00
\.


--
-- Data for Name: garden; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY garden (id, name, userid, location, location_reference) FROM stdin;
1	Garden 1            	1	Chesterfield, United Kingdom  	00000.84.03354                
2	Garden 2            	1	Chester, United Kingdom       	00000.35.03321                
3	Garden 3            	1	Chesterfield, United Kingdom  	00000.84.03354                
4	Garden4             	1	Birmingham, United Kingdom    	00000.54.03534                
5	GardenNew           	1	Cheam, United Kingdom         	00000.10.03781                
6	OtherUsersGarden    	3	Manchester, United Kingdom    	00000.53.03334                
7	DemoGarden          	1	Birmingham, United Kingdom    	00000.54.03534                
\.


--
-- Name: garden_id_seq; Type: SEQUENCE SET; Schema: public; Owner: stephen
--

SELECT pg_catalog.setval('garden_id_seq', 7, true);


--
-- Data for Name: gardenusers; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY gardenusers (gardenid, userid, editrights) FROM stdin;
1	1	t
2	1	t
3	1	t
1	2	f
4	1	t
5	1	t
6	3	t
6	1	f
7	1	t
\.


--
-- Data for Name: plants; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY plants (id, name, type, st1_days, st1_ow, st1_bw, st2_days, st2_ow, st2_bw, st3_days, st3_ow, st3_bw, st4_days, st4_ow, st4_bw, plants_persqm) FROM stdin;
1	Radish                        	1	5	1	0.5	10	1.3	0.6	15	1.6	0.8	5	0.5	0.1	2
2	Lettuce                       	1	20	1.5	1	30	2.5	1.8	15	3.5	3	10	2	0.5	8
3	Lentil                        	1	14	1	0.75	5	2	1.5	10	2.5	2.1	10	1	0.75	40
4	Tomato                        	2	15	20	10	15	15	8	20	28	25	15	18	18	5
5	Broccoli                      	1	35	5	2.5	86	4	2	56	8.5	4.25	14	3	1.5	2
6	Brussel Sprouts               	1	21	8.5	6.75	50	3	1	15	8	4	15	1	0	3
7	Raspberry                     	2	10	1	0.5	32	0.5	0	20	1.5	0.5	10	0.5	0	4
8	Basil                         	3	14	0.2	0.1	21	0.1	0	20	0.2	0.1	5	0.1	0	9
9	Strawberry                    	2	48	2	1.5	31	1	0.75	21	3	2.5	15	1	0.5	10
10	Potatoes (Main Crop)          	1	20	3	1	80	1	0.5	50	2	1	30	1	0	3
11	Beetroot                      	1	30	1	0.6	60	0.5	0.15	30	0.8	0.4	20	0.2	0.05	10
12	Cabbage                       	1	40	0.5	0.4	50	0.4	0.3	40	0.6	0.5	20	0.3	0.1	4
13	Carrot                        	1	20	0.6	0.5	25	0.5	0.3	30	0.7	0.5	15	0.4	0.1	80
14	Celery                        	1	20	10	8	100	9	7	40	12	10.3	20	8	7	9
15	Leek                          	1	60	0.5	0.2	75	0.4	0.1	15	0.5	0.3	10	0.2	0.1	12
16	Onion (bulbs)                 	1	25	4.27	3.8	40	2.5	2	30	3.5	2.5	30	0	0	9
17	Onion (salad)                 	1	30	30	20	40	20	10	20	30	20	15	15	0	8
18	Parsnip                       	1	30	6.7	5.5	40	5	40	40	6	5.5	20	2	1	10
19	Peas                          	1	30	60	50	30	50	30	20	90	75	20	60	50	15
20	Potatoes (Earlies)            	1	20	3	1	80	1	0.5	50	2	1	30	1	0	4
21	Shallots                      	1	30	4.27	2	60	3.5	1.5	30	3.9	2.3	20	2	0.5	16
22	Spinach                       	1	30	48	45	60	30	30	40	40	35	20	20	15	3
23	Turnip                        	1	30	33.55	15	40	15	7.5	30	20	10	20	5	0	20
24	Sweetcorn                     	1	28	45.5	40	38	40	30	40	67	60	10	20	0	15
25	Sage                          	3	25	1	0.5	35	1	0.25	40	1	0.25	900	0.5	0	4
26	Parsley                       	3	21	2	1.5	40	0.5	0	30	1.75	1	500	0.5	0	4
27	Chives                        	3	20	2	1	40	2	1	30	2	1	500	0.5	0	4
28	Black Currant                 	2	100	1	0.5	51	0.5	0.25	93	0.5	0.2	365	0	0	25
29	Blueberries                   	2	76	0.5	0.5	240	0.25	0.25	100	0	0	600	0	0	1
30	Rhubarb                       	2	200	3	2	200	2	1	370	3	1	500	0	0	4
31	Goosberry                     	2	175	0.5	0	75	0	0	60	0.25	0	500	0	0	2
32	White Currant                 	2	100	1	0.5	51	0.5	0.25	93	0.5	0.2	365	0	0	25
33	Red Currant                   	2	100	1	0.5	51	0.5	0.25	93	0.5	0.2	365	0	0	25
34	Figs                          	2	30	0.5	0.25	300	0.25	0	400	0	0	400	0	0	1
35	Hoeny Berries                 	2	20	5	3	365	0.25	0	400	0	0	400	0	0	2
36	Test Plant                    	1	5	1	1	5	0	0	5	2	2	5	1	1	2
\.


--
-- Name: plants_id_seq; Type: SEQUENCE SET; Schema: public; Owner: stephen
--

SELECT pg_catalog.setval('plants_id_seq', 36, true);


--
-- Data for Name: planttypes; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY planttypes (id, name) FROM stdin;
1	Vegetable           
2	Fruit               
3	Herb                
\.


--
-- Data for Name: plots; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY plots (id, name, size, plantid, gardenid, soiltypeid, environmentid, day_planted, month_planted, year_planted, priority) FROM stdin;
1	Plot 1	2	2	1	3	3	12	8	2017	2
5	Plot A	3	3	2	3	3	14	8	2017	2
10	plot56	4	5	1	3	3	14	8	2017	2
14	Plot1	3	11	3	3	3	19	8	2017	2
15	plot 2	3	14	3	3	3	19	8	2017	2
18	plot2	3	13	4	3	2	2	9	2017	2
17	Plot	3	2	4	3	3	30	8	2017	3
16	TestPlotZeroes	2	36	1	3	3	21	8	2017	3
21	Plot 1	2	5	5	3	3	26	8	2017	2
22	Plot 2	4	13	5	3	3	29	8	2017	2
23	Plot	3	16	1	3	3	26	8	2017	2
24	Lettuce Plot	2	2	6	3	3	30	8	2017	2
25	Radish Plot	4	1	6	3	3	30	8	2017	2
26	Lettuce Plot	2	2	7	3	2	30	8	2017	2
28	Lentil Plot	3	3	7	5	3	30	8	2017	2
29	RadishPlt2	3	1	7	3	3	30	8	2017	3
27	Radish Plot 1	3	1	7	3	3	30	8	2017	2
13	Test Plot2	3	11	1	3	3	24	8	2017	1
\.


--
-- Name: plots_id_seq; Type: SEQUENCE SET; Schema: public; Owner: stephen
--

SELECT pg_catalog.setval('plots_id_seq', 29, true);


--
-- Data for Name: soiltype; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY soiltype (id, name, value) FROM stdin;
1	Clay                	0.8
2	Part Clay           	0.9
3	Normal              	1.0
5	Sandy               	1.2
4	Part Sandy          	1.1
\.


--
-- Data for Name: test; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY test (name) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: stephen
--

COPY users (id, name, email, password) FROM stdin;
1	Stephen	user@useremail.com	123456
2	GardenUser2	useremail@gmail.com	myPassword
3	TestUser	example@example.com	123456
\.


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: stephen
--

SELECT pg_catalog.setval('users_id_seq', 3, true);


--
-- Name: environment_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY environment
    ADD CONSTRAINT environment_pkey PRIMARY KEY (id);


--
-- Name: garden_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY garden
    ADD CONSTRAINT garden_pkey PRIMARY KEY (id);


--
-- Name: plants_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plants
    ADD CONSTRAINT plants_pkey PRIMARY KEY (id);


--
-- Name: planttypes_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY planttypes
    ADD CONSTRAINT planttypes_pkey PRIMARY KEY (id);


--
-- Name: plots_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plots
    ADD CONSTRAINT plots_pkey PRIMARY KEY (id);


--
-- Name: soiltype_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY soiltype
    ADD CONSTRAINT soiltype_pkey PRIMARY KEY (id);


--
-- Name: users_email_key; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_email_key UNIQUE (email);


--
-- Name: users_pkey; Type: CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: garden_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY garden
    ADD CONSTRAINT garden_userid_fkey FOREIGN KEY (userid) REFERENCES users(id);


--
-- Name: gardenusers_gardenid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY gardenusers
    ADD CONSTRAINT gardenusers_gardenid_fkey FOREIGN KEY (gardenid) REFERENCES garden(id);


--
-- Name: gardenusers_userid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY gardenusers
    ADD CONSTRAINT gardenusers_userid_fkey FOREIGN KEY (userid) REFERENCES users(id);


--
-- Name: plants_type_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plants
    ADD CONSTRAINT plants_type_fkey FOREIGN KEY (type) REFERENCES planttypes(id);


--
-- Name: plots_environmentid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plots
    ADD CONSTRAINT plots_environmentid_fkey FOREIGN KEY (environmentid) REFERENCES environment(id);


--
-- Name: plots_gardenid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plots
    ADD CONSTRAINT plots_gardenid_fkey FOREIGN KEY (gardenid) REFERENCES garden(id);


--
-- Name: plots_plantid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plots
    ADD CONSTRAINT plots_plantid_fkey FOREIGN KEY (plantid) REFERENCES plants(id);


--
-- Name: plots_soiltypeid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: stephen
--

ALTER TABLE ONLY plots
    ADD CONSTRAINT plots_soiltypeid_fkey FOREIGN KEY (soiltypeid) REFERENCES soiltype(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

